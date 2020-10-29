package com.ghostj.server;

import com.ghostj.util.FileRW;
import com.ghostj.util.Out;

import java.io.File;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.TimerTask;

public class CheckAliveTimer extends TimerTask {
    public void run(){
        if(!ServerMain.manuallyTestConn) {
            try {
                ArrayList<HandleConn> dead = new ArrayList<>();
                for (HandleConn handleConn : ServerMain.socketArrayList) {
                    try {
                        handleConn.success = false;
                        CheckConnAlive cca = new CheckConnAlive(handleConn.bufferedWriter);
                        cca.start();
                        Thread.sleep(1000);
                        if (!handleConn.success)
                            dead.add(handleConn);
                    } catch (Exception e) {
                        dead.add(handleConn);
                    }
                }
                //Out.say("CheckAliveTimer","检查到"+dead.size()+"个失效连接");
                for (HandleConn d : dead) {
                    try {
                        ServerMain.killConn(d);
                    } catch (Exception e) {
                        Out.say("CheckAliveTimer", "清除失效连接时出现问题");
                        e.printStackTrace();
                    }
                }
                StringBuffer allOnlineClientList=new StringBuffer();
                for(HandleConn client:ServerMain.socketArrayList){
                    //写taglog
                    //写列表到文件以便rescueServer检测未启动客户端的机器
                    if(client.avai) {
                        ServerMain.tagLog.addTag(client.hostName, "alive");
                        allOnlineClientList.append("r"+client.hostName+" ");
                    }
                }
                FileRW.write("rescue"+ File.separatorChar+"onlineClients.txt",allOnlineClientList.toString());
                ServerMain.tagLog.addTag(".Server","alive");
                ServerMain.tagLog.pack();
                System.gc();

            }catch (ConcurrentModificationException e){
                ;
            } catch (Exception e){
                Out.say("CheckAliveTimer","检测连接时出错");
                e.printStackTrace();
            }
        }
    }
}
