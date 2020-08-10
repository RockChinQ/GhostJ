package com.ghostj.client;

import com.ghostj.util.Config;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Timer;

public class ClientMain {
    public static Config config;
    public static String ip;
    public static int port=33;
    public static SocketAddress socketAddress=null;
    public static Socket socket=null;
    public static BufferedReader bufferedReader=null;
    public static OutputStreamWriter bufferedWriter=null;
    public static HandleConn handleConn=null;
    public static String name="";

//    public static boolean processing=false;
    public static ProcessCmd focusedProcess=null;

    //以下是多任务支持的
    public static LinkedHashMap<String,ProcessCmd> processList=new LinkedHashMap<>();

    public static boolean success=false;

    public static long sysStartTime=-1;
    public static void main(String[] args){
        sysStartTime=new Date().getTime();
        //加载配置文件
        config=new Config("ghostjc.ini");
        port=config.getIntValue("port");
        ip=config.getStringValue("ip");
        socketAddress=new InetSocketAddress(ip,port);
        name=new String(config.getStringValue("name"));
        Out.say("ClientMain","config load done.port:"+port);
        //启动连接线程
        handleConn=new HandleConn();
        handleConn.start();
        //检测计时器
        Timer t=new Timer();
        t.schedule(new CheckAliveTimer(),1000,30000);
    }

    public static String getErrorInfo(Exception e){
        StringWriter sw=new StringWriter();
        PrintWriter pw=new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString().replaceAll("\t","    ");
    }
    public static void sendFinishToServer(){
        try{
            bufferedWriter.write("!finish!");
            bufferedWriter.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void removeProcess(String key){
        if(focusedProcess==processList.get(key))
            focusedProcess=null;
        try{
            System.out.println(processList.size()+" "+key);
            processList.remove(key);
            System.out.println(processList.size());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static boolean processing(){
        return processList.size()!=0;
    }
}
