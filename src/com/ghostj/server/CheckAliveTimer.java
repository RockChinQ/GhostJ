package com.ghostj.server;

import com.ghostj.client.ClientMain;
import com.sun.security.ntlm.Client;

import java.util.ArrayList;
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
                        new Thread().sleep(1500);
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
                dead = null;
                System.gc();
            }catch (Exception e){
                Out.say("CheckAliveTimer","检测连接时出错");
                e.printStackTrace();
            }
        }
    }
}
