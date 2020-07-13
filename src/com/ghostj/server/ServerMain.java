package com.ghostj.server;

import com.ghostj.util.Config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;

public class ServerMain {
    public static ArrayList<HandleConn> socketArrayList=new ArrayList<>();
    public static Config config=new Config("ghostjs.ini");
    public static int port=33;
    public static HandleConn focusedConn=null;
    public static TransCmd transCmd=null;
    public static AcceptConn acceptConn=null;
    public static void main(String[] args){
        //加载配置文件
        port=config.getIntValue("port");
        //加载服务端
        acceptConn=new AcceptConn();
        acceptConn.start();
        Out.say("ServerMain","监听器已启动 port:"+port);

        transCmd=new TransCmd();
        transCmd.start();
        Out.say("ServerMain","键盘>客户端数据传输接口已启动");

        //启动检查计时器
        Timer t=new Timer();
        t.schedule(new CheckAliveTimer(),1000,15000);
    }
    public static void killConn(HandleConn handleConn){
        if(handleConn==null)
            return;
        if(handleConn.equals(focusedConn))
            focusedConn=null;
        socketArrayList.remove(handleConn);
        Out.say("Main",handleConn.getName()+" 工作连接关闭，主机已断连");
        handleConn.stop();//管你那么多，过时的不安全的我也用
        Runtime.getRuntime().gc();
    }
//    public static void deleteConn(HandleConn conn){
//        socketArrayList.remove(conn);
//        conn.stop();
//    }
    public static void stopServer(int status){
        System.exit(status);
    }
}
