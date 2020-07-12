package com.ghostj.client;

import com.ghostj.util.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class ClientMain {
    public static Config config;
    public static String ip;
    public static int port=33;
    public static SocketAddress socketAddress=null;
    public static Socket socket=null;
    public static BufferedReader bufferedReader=null;
    public static BufferedWriter bufferedWriter=null;
    public static HandleConn handleConn=null;
    public static String name="";

    public static boolean processing=false;
    public static BufferedWriter processWriter=null;
    public static ProcessCmd processCmd=new ProcessCmd();
    public static CmdError cmdError=null;
    public static void main(String[] args){
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
    }
}
