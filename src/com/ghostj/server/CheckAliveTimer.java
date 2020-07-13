package com.ghostj.server;

import com.ghostj.client.ClientMain;
import com.sun.security.ntlm.Client;

import java.util.ArrayList;
import java.util.TimerTask;

public class CheckAliveTimer extends TimerTask {
    public void run(){
        if(!ServerMain.manuallyTestConn) {
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
                ServerMain.killConn(d);
            }
            dead = null;
            System.gc();
        }
    }
}
