package com.ghostj.client;

import sun.awt.windows.WBufferStrategy;

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
                ClientMain.bufferedWriter=new BufferedWriter(new OutputStreamWriter(ClientMain.socket.getOutputStream(),"UTF-8"));
                ClientMain.bufferedWriter.write("!name "+ClientMain.name+"!");
                //ClientMain.bufferedWriter.newLine();
                ClientMain.bufferedWriter.flush();
                Out.say("HandleConn","Sent name.");
                ClientMain.bufferedReader=new BufferedReader(new InputStreamReader(ClientMain.socket.getInputStream(),"UTF-8"));
                while(true){
                    String cmd= ClientMain.bufferedReader.readLine();
                    //接收到数据
                    //Out.say("HandleConn",""+cmd);

                    //处理
                    if(!ClientMain.processing){//无正在进行的操作
                        ClientMain.processCmd=new ProcessCmd();
                        ClientMain.processCmd.cmd=new String(cmd);
                        ClientMain.processCmd.start();

                    }else {//正在进行
                        //检查是否是工作指令
                        String cmd0[]=cmd.split(" ");
                        switch (cmd0[0]){
                            case "!!kill":{
                                ClientMain.cmdError.stop();
                                ClientMain.processCmd.process.destroy();
                                ClientMain.processCmd.stop();
                                ClientMain.processing=false;
                                continue;
                            }
                        }
                        System.out.print("passing cmd");
                        ClientMain.processWriter.write(cmd);
                        ClientMain.processWriter.newLine();
                        ClientMain.processWriter.flush();
                    }
                }
            }catch (Exception e){

                Out.say("HandleConn","连接异常，尝试重新连接");
                e.printStackTrace();

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
