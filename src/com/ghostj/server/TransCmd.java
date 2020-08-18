package com.ghostj.server;

import com.ghostj.util.FileRW;
import com.ghostj.util.Out;
import com.ghostj.util.TimeUtil;
import com.rft.core.server.BufferedFileReceiver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class TransCmd extends  Thread{

    public void run(){
        BufferedReader typeReader = new BufferedReader(new InputStreamReader(System.in));//键盘的reader
        readMsg:while (true) {
            try {
                String typeCmd = typeReader.readLine();
                //检查是否是工作信息
                String cmd[]=typeCmd.split(" ");
                handleCommand(typeCmd);
            }catch (Exception e){
                e.printStackTrace();
                Out.say("TansCmd","处理指令失败");
                ServerMain.cmdProcessFinish();
            }
        }
    }
    public void handleCommand(String typeCmd) throws IOException {
        try {
            Out.say(typeCmd);
            String cmd[]=typeCmd.split(" ");
            switch (cmd[0]) {
                case "!help": {
                    if(new File(("serverHelp.txt")).exists())
                        Out.say("TransCmd-help", FileRW.read("serverHelp.txt").replaceAll("ln", "\n\r"));
                    else {
                        Out.say("TransCme-help", "command      description\n!list   列表所有连接的主机\n!focus <connName(wordStartwith)>   聚焦\n!dfocus    退出聚焦\n!chname <connName(wordStartWith)> <newName>     修改客户端名称\n!stop    关闭服务端\n!close    关闭服务端" +
                                "\n!pw <newPassword> 修改master连接的密码" +
                                "\n!test [timeout] 测试每个客户端连接，period为超时时间");
                    }
                    ServerMain.cmdProcessFinish();
                    return;
                }
                case "!list": {
                    Out.say("TransCmd-info", "已建立的连接(" + ServerMain.socketArrayList.size() + ")：\nindex\tname\tconnTime\tstate\tversion");
                    for (HandleConn conn : ServerMain.socketArrayList) {
                        Date d = new Date(conn.connTime);
                        Out.say(conn.rtIndex + "\t" + conn.hostName + "\t" + (d.getDate() + "," + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds()) + "\t"
                                + (conn.equals(ServerMain.focusedConn) ? "聚焦" : "后台")+"\t"+conn.version);
                    }
                    Out.say("TransCmd-info", "列表完成.");
                    ServerMain.cmdProcessFinish();
                    return;
                }
                case "!echo":{
                    if (cmd.length>1){
                        Out.sayThisLine("[TransCmd-echo]");
                        for(int i=1;i<cmd.length;i++){
                            Out.sayThisLine(cmd[i]+" ");
                        }
                    }
                    Out.sayThisLine("\n");
                    return;
                }
                case "!pw": {
                    ServerMain.masterPw = cmd[1];
                    ServerMain.config.set("master.pw", cmd[1]);
                    ServerMain.config.write();
                    Out.say("TransCmd-pw", "master新密码:" + ServerMain.masterPw);
                    ServerMain.cmdProcessFinish();
                    return;
                }
                case "!dfocus": {
                    ServerMain.focusedConn = null;
                    Out.say("TransCmd-dfocus", "取消聚焦");
                    ServerMain.sendListToMaster();
                    ServerMain.cmdProcessFinish();
                    return;
                }
                case "!focus": {
                    if (cmd.length < 2) {
                        Out.say("TransCmd-focus", "正确语法\n!focus <connName(wordStartWith)>\n!focus %<index>");
                        ServerMain.cmdProcessFinish();
                        return;
                    }
                    if (cmd[1].startsWith("&")) {
                        try {
                            long i = Integer.parseInt(cmd[1].substring(1));
                            if(ServerMain.focusedConn!=null&&ServerMain.focusedConn.rtIndex==i)//已经聚焦到目标
                                return;
                            //Out.say("index:" + i);
                            for (HandleConn conn : ServerMain.socketArrayList) {
                                if (conn.rtIndex == i) {
                                    ServerMain.focusedConn = conn;
                                    Out.say("TransCmd-focus", "聚焦" + ServerMain.focusedConn.hostName);
                                    ServerMain.focusedConn.showHistory();
                                    Out.say("TransCmd-focus","===========历史消息==========");
                                    ServerMain.sendListToMaster();
                                    ServerMain.cmdProcessFinish();
                                    return;
                                }
                            }
                        } catch (Exception e) {
                            Out.say("TransCmd-focus", "正确语法\n!focus &<index>");
                        }
                    } else {
                        if(ServerMain.focusedConn!=null&&ServerMain.focusedConn.hostName.startsWith(cmd[1])){//已经聚焦到目标
                            return;
                        }
                        for (HandleConn conn : ServerMain.socketArrayList) {
                            if (conn.hostName.startsWith(cmd[1])) {
                                ServerMain.focusedConn = conn;
                                Out.say("TransCmd-focus", "聚焦" + ServerMain.focusedConn.hostName);
                                ServerMain.focusedConn.showHistory();
                                Out.say("=========历史消息========");
                                ServerMain.sendListToMaster();
                                ServerMain.cmdProcessFinish();
                                return;
                            }
                        }
                    }
                    Out.say("TransCmd-focus", "无法找到连接");
                    ServerMain.cmdProcessFinish();
                    return;
                }
                case "!test": {
                    int time = 1500;
                    try {
                        time = Integer.parseInt(cmd[1]);
                    } catch (Exception e) {
                        Out.say("可设置间隔时间");
                        time = 1500;
                    }
                    new CheckAliveMaster(time).start();
                    return;
                }
                case "!chname": {
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
                                ServerMain.sendListToMaster();
                                ServerMain.cmdProcessFinish();
                                return;
                            }
                        }
                        Out.say("TransCmd-chname", "无此名称连接");
                    } else {
                        Out.say("TransCmd-chname", "正确语法\n!chname <connName(wordStartWith)> <newName>");
                    }
                    ServerMain.cmdProcessFinish();
                    return;
                }
                case "!rmtag":{
                    if(cmd.length<2){
                        Out.say("TransCmd-rmtag","正确语法\n!rmtag <tagOwnerName(wordStartWith)>");
                        ServerMain.cmdProcessFinish();
                        return;
                    }
                    ArrayList<String> delete=new ArrayList<>();
                    for(String owner:ServerMain.tagLog.allOwner.keySet()){
                        if(owner.startsWith(cmd[1])){
                            delete.add(owner);
                        }
                    }
                    if(delete.size()!=0){
                        for(String dk:delete){
                            Out.say("TransCmd-rmtag","清除tagLog:"+dk);
                            ServerMain.tagLog.allOwner.remove(dk);
                        }
                        ServerMain.tagLog.pack();
                    }else {
                        Out.say("TransCmd-rmtag","未找到相应的tagOwner");
                    }
                    ServerMain.cmdProcessFinish();
                    return;
                }
                case "!lstag":{
                    Out.say("TransCmd-lstag","列表所有tagLog");
                    int i=0;
                    for(String owner:ServerMain.tagLog.allOwner.keySet()){
                        Out.say(++i+" "+owner);
                    }
                    Out.say("TransCmd-lstag","列表完成");
                    ServerMain.cmdProcessFinish();
                    return;
                }
                case "!lsmst":{
                    Out.say("TransCmd-lsmst","列出所有在线master的信息");
                    int i=0;
                    for(HandleMaster master:AcceptMaster.masters){
                        Out.say(i+++"  ip"+master.socket.getInetAddress()+"  t"+ TimeUtil.millsToMMDDHHmmSS(master.connTime));
                    }
                    Out.say("TransCmd-lsmst","列表完成");
                    ServerMain.cmdProcessFinish();
                    return;
                }
                case "!rft":{
                    if(cmd.length<2){
                        Out.say("TransCmd-rft","命令语法不正确");
                        ServerMain.cmdProcessFinish();
                        return;
                    }
                    switch (cmd[1]){
                        case "chdir":{//修改文件保存根目录
                            if(cmd.length<3){
                                Out.say("TransCmd-rft chdir","命令语法不正确");
                                ServerMain.cmdProcessFinish();
                                return;
                            }
                            ServerMain.fileReceiver.setRootPath(cmd[2]);
                            Out.say("TransCmd-rft chdir","已修改文件服务器的保存根目录为"+cmd[2]);
                            ServerMain.cmdProcessFinish();
                            return;
                        }
                        case "dir":{
                            Out.say("TransCmd-rft dir","文件服务器的根目录是"+ServerMain.fileReceiver.getRootPath());
                            ServerMain.cmdProcessFinish();
                            return;
                        }
                        case "task":{
                            
                        }
                    }
                    ServerMain.cmdProcessFinish();
                    return;
                }
                //TODO 删除这个过时命令
                case "!chrftd":{
                    if(cmd.length<2){
                        Out.say("TransCmd-chrftd","命令语法不正确");
                        return;
                    }
                    ServerMain.fileReceiver.setRootPath(cmd[1]);
                    Out.say("TransCmd-chrftd","文件接收根目录为:"+ServerMain.fileReceiver.getRootPath());
                    return;
                }
                case "!close":
                case "!stop": {
                    Out.say("TransCmd-stop", "关闭服务端");
                    ServerMain.cmdProcessFinish();
                    ServerMain.stopServer(0);
                }
            }
            //传输指令
            if (ServerMain.focusedConn == null) {
                Out.say("TransCmd", "没有聚焦任何连接");
                ServerMain.cmdProcessFinish();
                return;
            }
            try {
                ServerMain.focusedConn.bufferedWriter.write(typeCmd);
                ServerMain.focusedConn.bufferedWriter.newLine();
                ServerMain.focusedConn.bufferedWriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
                Out.say("TransCmd", "指令传输失败");
                ServerMain.killConn(ServerMain.focusedConn);
                ServerMain.cmdProcessFinish();
            }
        }catch (Exception e){
            throw e;
        }
    }
}
