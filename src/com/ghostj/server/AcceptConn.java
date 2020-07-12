package com.ghostj.server;

import java.net.ServerSocket;
import java.net.Socket;

public class AcceptConn extends Thread{
    public void run(){
        try{
            ServerSocket serverSocket=new ServerSocket(ServerMain.port);

            while (true){
                Socket socket=serverSocket.accept();//接受连接
                Out.say("AcceptConn","新连接正在接受");
                HandleConn handleConn=new HandleConn(socket);
                ServerMain.socketArrayList.add(handleConn);
                handleConn.start();
                if(ServerMain.focusedConn==null){
                    ServerMain.focusedConn=handleConn;
                    Out.say("AcceptConn","自动聚焦到新连接");
                }
            }
        }catch(Exception e){
            Out.say("AcceptConn","接受连接出错");
            e.printStackTrace();
        }
    }
}