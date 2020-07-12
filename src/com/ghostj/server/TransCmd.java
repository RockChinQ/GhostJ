package com.ghostj.server;

import com.sun.corba.se.spi.activation.Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;

public class TransCmd extends  Thread{

    public void run(){
        BufferedReader typeReader = new BufferedReader(new InputStreamReader(System.in));//键盘的reader
        readMsg:while (true) {
            try {
                String typeCmd = typeReader.readLine();
                //检查是否是工作信息
                String cmd[]=typeCmd.split(" ");
                switch (cmd[0]){
                    case "!list":{
                        Out.say("TransCmd-info","已建立的连接("+ServerMain.socketArrayList.size()+")：\n\t\tname\tconnTime\tstate");
                        for(HandleConn conn:ServerMain.socketArrayList){
                            Out.say(conn.hostName+"  "+conn.connTime+"   "
                                    +(conn.equals(ServerMain.focusedConn)?"聚焦":"后台"));
                        }
                        Out.say("TransCmd-info","列表完成.");
                        continue;
                    }
                    case "!dfocus":{
                        ServerMain.focusedConn=null;
                        Out.say("TransCmd-dfocus","取消聚焦");
                        continue readMsg;
                    }
                    case "!focus":{
                        for(HandleConn conn:ServerMain.socketArrayList){
                            if(conn.hostName.startsWith(cmd[1])){
                                ServerMain.focusedConn=conn;
                                Out.say("TransCmd-focus","聚焦"+ ServerMain.focusedConn.hostName);
                                continue readMsg;
                            }
                        }
                        Out.say("TransCmd-focus","无此名称连接");
                        continue;
                    }
                    case "!close":
                    case "!stop":{
                        Out.say("TransCmd-stop","关闭服务端");
                        ServerMain.stopServer(0);
                    }
                }
                //传输指令
                if (ServerMain.focusedConn==null) {
                    Out.say("TransCmd", "没有聚焦任何连接");
                    continue;
                }
                ServerMain.focusedConn.bufferedWriter.write(typeCmd);
                ServerMain.focusedConn.bufferedWriter.newLine();
                ServerMain.focusedConn.bufferedWriter.flush();
            }catch (Exception e){
                e.printStackTrace();
                Out.say("TansCmd","无法将输入指令传输至客户端");
            }
        }
    }
}
