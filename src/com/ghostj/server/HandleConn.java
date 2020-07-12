package com.ghostj.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class HandleConn extends Thread{
    Socket socket=null;
    BufferedWriter bufferedWriter=null;
    String hostName=new Date().getTime()+"";
    long connTime=0;
    ArrayList<String> msg=new ArrayList<>();//在没有焦点的时候存储收到的消息
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
            InputStreamReader inputStreamReader=new InputStreamReader(socket.getInputStream(),"utf8");
            int c=0;
            while ((c=inputStreamReader.read())!=-1){
                //String msg=bufferedReader.readLine();
                //检查是否是工作信息
                if((char)c=='!'){
                    StringBuffer cmds=new StringBuffer("");
                    int workInfoLen=0;
                    while((c=inputStreamReader.read())!=-1){
                        if((char)c=='!') {
                            //cmds.append("");
                            break;
                        }
                        if ((char)c=='\n') {
                            cmds.append("\n");
                            break;
                        }
                        cmds.append((char)c);
                        workInfoLen++;
                        if(workInfoLen>=25)
                            break;
                    }
                    String cmd[]=cmds.toString().split(" ");
                    switch (cmd[0]){
                        case "name":{
                            Out.say("conn"+hostName,"新名称"+cmd[1]);
                            this.hostName=new String(cmd[1]);
                            continue;
                        }
                        default:{
                            Out.sayThisLine(cmds.toString());
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
