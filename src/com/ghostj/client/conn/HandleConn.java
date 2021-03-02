package com.ghostj.client.conn;

import com.ghostj.client.cmd.CommandProcessException;
import com.ghostj.client.core.ClientMain;
import com.ghostj.client.util.FileRW;
import com.ghostj.client.util.Out;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
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
        try {
            ClientMain.processor.start("!!startup");
        } catch (CommandProcessException e) {
            e.printStackTrace();
        }
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
                outToServer=new OutputStreamWriter(socket.getOutputStream(),"GBK");
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

    /**
     * 向服务端发送消息
     * @param msg
     */
    public static void writeToServer(String msg)throws Exception{

        getOutToServer().write(msg);
        getOutToServer().flush();
    }
    /**
     * 向服务端发送消息
     * @param msg
     */
    public static void writeToServerIgnoreException(String msg){
        try {
            getOutToServer().write(msg);
            getOutToServer().flush();
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
