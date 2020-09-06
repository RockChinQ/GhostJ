package com.ghostj.client.core;

import com.ghostj.util.TimeUtil;
import com.rft.core.client.FileSender;

import java.io.*;
import java.net.Socket;
import java.util.Date;

public class HandleConn extends Thread{
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
    @Override
    public void run(){
        //反复尝试连接
        while (true){
            try {
                long st=new Date().getTime();
                try {
                    socket=new Socket();
                    socket.connect(com.ghostj.client_old.ClientMain.socketAddress, 30000);
                } catch (IOException e) {
                    Out.say("HandleConn","无法建立连接，正在尝试重新连接");
                    e.printStackTrace();
                    try{ sleep(10000-(new Date().getTime()-st)); }catch (Exception ignored){ ; }
                    continue;
                }
                /**
                 * 为io对象赋值
                 */
                outToServer=new OutputStreamWriter(com.ghostj.client_old.ClientMain.socket.getOutputStream(),"GBK");
                readFromServer=new BufferedReader(new InputStreamReader(socket.getInputStream(),"GBK"));
                //连接正常
                Out.say("HandleConn","已连接");
                //发送info
                writeToServer("!info "+ClientMain.name+" c"+ FileRW.read("nowVer.txt")
                        +" "+ ClientMain.sysStartTime+"!");
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
    public static void writeToServer(String msg){

        try {
            getOutToServer().write(msg);
            getOutToServer().flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 向服务端发送命令执行完毕的信息
     */
    public static void sendFinishToServer(){
        try{
            HandleConn.writeToServer("!finish!");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
