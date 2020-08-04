package com.ghostj.server;

import com.ghostj.util.Out;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

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
            }
        }
    }
    public void handleCommand(String typeCmd) throws IOException {
        try {
            String cmd[]=typeCmd.split(" ");
            switch (cmd[0]) {
                case "!help": {
                    Out.say("TransCme-help", "command      description\n!list   列表所有连接的主机\n!focus <connName(wordStartwith)>   聚焦\n!dfocus    退出聚焦\n!chname <connName(wordStartWith)> <newName>     修改客户端名称\n!stop    关闭服务端\n!close    关闭服务端" +
                            "\n!pw <newPassword> 修改master连接的密码" +
                            "\n!test [timeout] 测试每个客户端连接，period为超时时间");
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
                    return;
                }
                case "!pw": {
                    ServerMain.masterPw = cmd[1];
                    ServerMain.config.set("master.pw", cmd[1]);
                    ServerMain.config.write();
                    Out.say("TransCmd-pw", "master新密码:" + ServerMain.masterPw);
                    return;
                }
                case "!dfocus": {
                    ServerMain.focusedConn = null;
                    Out.say("TransCmd-dfocus", "取消聚焦");
                    ServerMain.sendListToMaster();
                    return;
                }
                case "!focus": {
                    if (cmd.length < 2) {
                        Out.say("TransCmd-focus", "正确语法\n!focus <connName(wordStartWith)>\n!focus %<index>");
                        return;
                    }
                    if (cmd[1].startsWith("&")) {
                        try {
                            long i = Integer.parseInt(cmd[1].substring(1));
                            //Out.say("index:" + i);
                            for (HandleConn conn : ServerMain.socketArrayList) {
                                if (conn.rtIndex == i) {
                                    ServerMain.focusedConn = conn;
                                    Out.say("TransCmd-focus", "聚焦" + ServerMain.focusedConn.hostName);
                                    ServerMain.sendListToMaster();
                                    return;
                                }
                            }
                        } catch (Exception e) {
                            Out.say("TransCmd-focus", "正确语法\n!focus %<index>");
                        }
                    } else {
                        for (HandleConn conn : ServerMain.socketArrayList) {
                            if (conn.hostName.startsWith(cmd[1])) {
                                ServerMain.focusedConn = conn;
                                Out.say("TransCmd-focus", "聚焦" + ServerMain.focusedConn.hostName);
                                ServerMain.sendListToMaster();
                                return;
                            }
                        }
                    }
                    Out.say("TransCmd-focus", "无法找到连接");
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
                                return;
                            }
                        }
                        Out.say("TransCmd-chname", "无此名称连接");
                    } else {
                        Out.say("TransCmd-chname", "正确语法\n!chname <connName(wordStartWith)> <newName>");
                    }
                    return;
                }
                case "!rmtag":{
                    if(cmd.length<2){
                        Out.say("TransCmd-rmtag","正确语法\n!rmtag <tagOwnerName(wordStartWith)>");
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
                    return;
                }
                case "!close":
                case "!stop": {
                    Out.say("TransCmd-stop", "关闭服务端");
                    ServerMain.stopServer(0);
                }
            }
            //传输指令
            if (ServerMain.focusedConn == null) {
                Out.say("TransCmd", "没有聚焦任何连接");
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
            }
        }catch (Exception e){
            throw e;
        }
    }
}
