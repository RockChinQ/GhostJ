package com.ghostj.client;

//import sun.awt.windows.WBufferStrategy;

import com.ghostj.util.FileRW;

import java.io.*;
import java.net.Socket;
import java.util.Date;

public class HandleConn extends Thread{
    public void run(){
        //反复尝试连接
        while (true){
            try {
                long st=new Date().getTime();
                try {
                    ClientMain.socket=new Socket();
                    ClientMain.socket.connect(ClientMain.socketAddress, 30000);
                } catch (IOException e) {
                    Out.say("HandleConn","无法建立连接，正在尝试重新连接");
                    e.printStackTrace();
                    try{ this.sleep(15000-(new Date().getTime()-st)); }catch (Exception e0){ ; }
                    continue;
                }
                //连接正常
                Out.say("HandleConn","已连接");
                //发送name
                ClientMain.bufferedWriter=new OutputStreamWriter(ClientMain.socket.getOutputStream(),"UTF-8");
                ClientMain.bufferedWriter.write("!name "+ClientMain.name+"!");
                //ClientMain.bufferedWriter.newLine();
                ClientMain.bufferedWriter.flush();
                Out.say("HandleConn","Sent name.");
                ClientMain.bufferedReader=new BufferedReader(new InputStreamReader(ClientMain.socket.getInputStream(),"UTF-8"));
                //发送版本号
                String ver= FileRW.read("nowVer.txt");
                ClientMain.bufferedWriter.write("!version "+ver+"!");
                ClientMain.bufferedWriter.flush();
                while(true){
                    String cmd= ClientMain.bufferedReader.readLine();
                    //接收到数据
                    //Out.say("HandleConn",""+cmd);

                    //检查是否是工作指令
                    String cmd0[]=cmd.split(" ");
                    switch (cmd0[0]){
                        case "#alive#":{
                            ClientMain.success=true;
                            continue;
                        }
                        case "#alives#":{
                            ClientMain.bufferedWriter.write("!alives!");
                            ClientMain.bufferedWriter.flush();
                            continue;
                        }
                        case "!!reconn":{
                            ClientMain.socket.close();
                            continue;
                        }
                        case "!!help":{
                            String helpinfo="help msg:\ncommand        description\n!!kill     kill the current process(use when a command last for so long)\n!!listcfg   list all config fields of client\n!!rmcfg <key>   remove a field\n!!cfg <key> <value>   set a field\n!!writecfg   write config fields to file";
                            ClientMain.bufferedWriter.write(helpinfo+"\n");
                            ClientMain.bufferedWriter.flush();
                            continue;
                        }
                        case "!!name":{
                            if(cmd0.length<2){
                                ClientMain.bufferedWriter.write("正确语法\n!!name <newName>\n" );
                                ClientMain.bufferedWriter.flush();
                                continue;
                            }
                            ClientMain.name=cmd0[1];
                            ClientMain.config.set("name",cmd0[1]);
                            ClientMain.bufferedWriter.write("已修改名称为"+cmd0[1]+"\n" );
                            ClientMain.bufferedWriter.flush();
                            continue;
                        }
                        case "!!kill":{
                            if(ClientMain.processing) {
                                ClientMain.cmdError.stop();
                                ClientMain.processCmd.process.destroy();
                                ClientMain.processCmd.stop();
                                ClientMain.processing = false;
                                ClientMain.bufferedWriter.write("已终止当前任务\n");
                                //ClientMain.bufferedWriter.newLine();
                                ClientMain.bufferedWriter.flush();
                            }else{
                                ClientMain.bufferedWriter.write("无任务正在进行\n");
                                //ClientMain.bufferedWriter.newLine();
                                ClientMain.bufferedWriter.flush();
                            }
                            continue;
                        }
                        case "!!listcfg":{
                            StringBuffer fields=new StringBuffer("客户端"+ClientMain.name+"的配置文件字段\n");
                            for(String key:ClientMain.config.field.keySet()){
                                fields.append(key+"="+ClientMain.config.field.get(key)+"\n");
                            }
                            fields.append("列表完成");
                            ClientMain.bufferedWriter.write(fields.toString()+"\n");
                            ClientMain.bufferedWriter.flush();
                            continue;
                        }
                        case "!!rmcfg":{
                            if(cmd0.length>=2) {
                                try {
                                    ClientMain.config.field.remove(cmd0[1]);
                                    ClientMain.bufferedWriter.write("success\n");
                                    ClientMain.bufferedWriter.flush();
                                }catch (Exception e){
                                    ClientMain.bufferedWriter.write("删除失败"+e.getStackTrace()+"\n");
                                    ClientMain.bufferedWriter.flush();
                                }
                            }else{
                                ClientMain.bufferedWriter.write("正确语法\n!!rmcfg <key>\n" );
                                ClientMain.bufferedWriter.flush();
                            }
                            continue;
                        }
                        case "!!cfg":{
                            if(cmd0.length>=3) {
                                ClientMain.config.set(cmd0[1], cmd0[2]);
                                ClientMain.bufferedWriter.write("设置" + cmd0[1] + "=" + cmd0[2]+"\n");
                                //ClientMain.bufferedWriter.newLine();
                                ClientMain.bufferedWriter.flush();
                                Out.say("HandleConn","设置"+cmd0[1]+"="+cmd0[2]);
                            }else{
                                ClientMain.bufferedWriter.write("正确语法:\n!!cfg <key> <value>\n");
                                //ClientMain.bufferedWriter.newLine();
                                ClientMain.bufferedWriter.flush();

                            }
                            continue;
                        }
                        case "!!writecfg":{
                            ClientMain.config.write();
                            ClientMain.bufferedWriter.write("配置文件已写入\n");
                            //ClientMain.bufferedWriter.newLine();
                            ClientMain.bufferedWriter.flush();
                            continue;
                        }
                    }
                    //处理收到的消息
                    if(!ClientMain.processing){//无正在进行的操作
                        ClientMain.processCmd=new ProcessCmd();
                        ClientMain.processCmd.cmd=new String(cmd);
                        ClientMain.processCmd.start();

                    }else {//正在进行
                        System.out.print("passing cmd");
                        ClientMain.processWriter.write(cmd);
                        ClientMain.processWriter.newLine();
                        ClientMain.processWriter.flush();
                    }
                }
            }catch (Exception e){

                Out.say("HandleConn","连接异常，尝试重新连接");
                e.printStackTrace();
                try {
                    ClientMain.socket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                continue;
            }finally {
                try {
                    ClientMain.socket.close();
                }catch (Exception e){
                    ;
                }
            }
        }
    }
}
