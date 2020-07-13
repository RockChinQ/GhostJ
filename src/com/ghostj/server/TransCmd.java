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
                    case "!help":{
                        Out.say("TransCme-help", "command      description\n!list   列表所有连接的主机\n!focus <connName(wordStartwith)>   聚焦\n!dfocus    退出聚焦\n!chname <connName(wordStartWith)> <newName>     修改客户端名称\n!stop    关闭服务端\n!close    关闭服务的");
                        continue;
                    }
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
                        if(cmd.length<2){
                            Out.say("TransCmd-focus","正确语法\n!focus <connName(wordStartWith)>");
                            continue ;
                        }
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
                    case "!test":{
                        new CheckAliveMaster().start();
                    }
                    case "!chname":{
                        if (cmd.length >= 3) {
                            for (HandleConn conn : ServerMain.socketArrayList) {
                                if (conn.hostName.startsWith(cmd[1])) {
                                    conn.hostName = new String(cmd[2]);
                                    conn.bufferedWriter.write("!!name " + cmd[2]);
                                    conn.bufferedWriter.newLine();
                                    conn.bufferedWriter.flush();
                                    conn.bufferedWriter.write("!!writecfg");
                                    conn.bufferedWriter.newLine();
                                    conn.bufferedWriter.flush();
                                    Out.say("TransCmd-chname", "已修改名称");
                                    continue readMsg;
                                }
                            }
                            Out.say("TransCmd-chname", "无此名称连接");
                        }else{
                            Out.say("TransCmd-chname","正确语法\n!chname <connName(wordStartWith)> <newName>");
                        }
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
                try {
                    ServerMain.focusedConn.bufferedWriter.write(typeCmd);
                    ServerMain.focusedConn.bufferedWriter.newLine();
                    ServerMain.focusedConn.bufferedWriter.flush();
                }catch (Exception e){
                    e.printStackTrace();
                    Out.say("TransCmd","指令传输失败");
                    ServerMain.killConn(ServerMain.focusedConn);
                }
            }catch (Exception e){
                e.printStackTrace();
                Out.say("TansCmd","处理指令失败");
            }
        }
    }
}
