package com.ghostj.client;

import com.ghostj.util.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Date;
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

    public static boolean processing=false;
    public static BufferedWriter processWriter=null;
    public static ProcessCmd processCmd=new ProcessCmd();
    public static CmdError cmdError=null;

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
}
