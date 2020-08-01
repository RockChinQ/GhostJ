package com.ghostj.client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Downloader {
    static final int BUFFER_SIZE=500;
    /**
     * 从网络Url中下载文件
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public static void  downLoadFromUrl(String urlStr,String fileName,String savePath,String toekn) throws IOException{
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        conn.setRequestProperty("lfwywxqyh_token",toekn);

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        File file = new File(saveDir+File.separator+fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if(fos!=null){
            fos.close();
        }
        if(inputStream!=null){
            inputStream.close();
        }


        System.out.println("info:"+url+" download success");

    }
    /**
     * 更加可靠的下载文件方法，实时保存至文件
     * @param urlStr
     * @param fileName
     * @param savePath
     * @param token
     * */
    public static void downloadFromUrl(String urlStr,String fileName,String savePath,String token)throws Exception{
        URL url=new URL(urlStr);
        HttpURLConnection conn=(HttpURLConnection)url.openConnection();

        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        conn.setRequestProperty("lfwywxqyh_token",token);

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
        //文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        File file = new File(saveDir+File.separator+fileName);

        ClientMain.bufferedWriter.write(urlStr+"->"+file.getAbsolutePath());
        ClientMain.bufferedWriter.flush();
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter=new OutputStreamWriter(fos);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        //缓存用的数组
        char buffer[]=new char[BUFFER_SIZE];
        int len=0;
        while ((len=inputStreamReader.read(buffer))!=-1){
            outputStreamWriter.write(buffer,0,len);
            outputStreamWriter.flush();
        }
        if (outputStreamWriter!=null){
            outputStreamWriter.close();
        }
        if (inputStreamReader!=null){
            inputStreamReader.close();
        }
    }



    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

}
