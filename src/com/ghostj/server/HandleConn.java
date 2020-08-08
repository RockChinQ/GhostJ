package com.ghostj.server;

import com.ghostj.util.Out;

import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class HandleConn extends Thread{
    Socket socket=null;
    BufferedWriter bufferedWriter=null;
    public String hostName=new Date().getTime()+"";
    boolean avai=false;
    long connTime=0;
    long sysStartTime=0;
    ArrayList<String> msg=new ArrayList<>();//在没有焦点的时候存储收到的消息
    long rtIndex=-1;

    String version=null;
    boolean success=false;
    public HandleConn(Socket socket){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"GBK"));
            connTime=new Date().getTime();
            Out.say("conn"+hostName,"连接已建立");
        }catch(Exception e){
            Out.say("conn"+hostName,"新连接建立时出现错误，已拒绝连接");
            e.printStackTrace();
            ServerMain.killConn(this);
        }
    }

    @Override
    public void run() {
        try{
            //BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
            InputStreamReader inputStreamReader=new InputStreamReader(socket.getInputStream(),"GBK");
            int c=0;
            while ((c=inputStreamReader.read())!=-1){
                //String msg=bufferedReader.readLine();
                //检查是否是工作信息
                if((char)c=='!'){
                    StringBuffer cmds=new StringBuffer("!");
                    int workInfoLen=0;
                    while((c=inputStreamReader.read())!=-1){
                        if((char)c=='!') {
                            cmds.append("!");
                            break;
                        }
                        if ((char)c=='\n') {
                            cmds.append("\n");
                            break;
                        }
                        cmds.append((char)c);
                        workInfoLen++;
//                        if(workInfoLen>=25)
//                            break;
                    }
                    String cmd[]=cmds.toString().substring(0,cmds.length()-1).split(" ");
                    switch (cmd[0]){
                        case "!alive":{
                            bufferedWriter.write("#alive#");
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                            continue;
                        }
                        case "!alives":{
                            success=true;
                            continue;
                        }
                        case "!name":{//此开关已过时，新版本的client使用info指令传输属性信息
                            Out.say("conn"+hostName,"名称"+cmd[1]);
                            this.hostName=new String(cmd[1]);
                            ServerMain.sendListToMaster();
                            Out.putPrompt();
                            avai=true;
                            ServerMain.tagLog.addTag(this.hostName,"login");
                            ServerMain.tagLog.addTag(this.hostName,"alive");
                            ServerMain.tagLog.pack();
                            continue;
                        }
                        case "!version":{
                            if(cmd.length<2){
                                Out.say("conn"+hostName,"客户端提供了不正确的版本号消息");
                                continue;
                            }
                            Out.say("conn"+hostName,"版本号:"+cmd[1]);
                            this.version=new String(cmd[1]);
                            ServerMain.sendListToMaster();
                            continue;
                        }
                        case "!finish":{
                            ServerMain.cmdProcessFinish();
                            continue;
                        }
                        case "!info":{
                            if (cmd.length<4){
                                Out.say("conn"+hostName,"客户端提供的info语法不正确");
                                Out.say("conn"+hostName,"info:"+cmds);
                                continue;
                            }
                            Out.say("conn"+cmd[1],"name:"+cmd[1]+" version:"+cmd[2]+" sysStartTime:"+cmd[3]);
                            Out.putPrompt();
                            avai=true;
                            this.hostName=cmd[1];
                            this.version=cmd[2];
                            this.sysStartTime=Long.parseLong(cmd[3]);
                            ServerMain.sendListToMaster();

                            ServerMain.tagLog.addTag(this.hostName,"login");
                            ServerMain.tagLog.addTag(this.hostName,"alive");
                            ServerMain.tagLog.pack();
                            continue;
                        }
                        default:{
                            Out.sayThisLine(cmds.toString());
                            continue;
                        }
                    }
                }
                //输出消息
                if(this.equals(ServerMain.focusedConn)){
                    Out.sayThisLine((char)c);
                }else {
                    this.msg.add((char)c+"");
                }
            }
        }catch(Exception e){
            Out.say("conn"+hostName,"处理工作连接时错误，工作连接已关闭");
            e.printStackTrace();
            ServerMain.killConn(this);
        }
    }
}
