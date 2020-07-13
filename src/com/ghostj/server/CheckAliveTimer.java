package com.ghostj.server;

import com.ghostj.client.ClientMain;
import com.sun.security.ntlm.Client;

import java.util.ArrayList;
import java.util.TimerTask;

public class CheckAliveTimer extends TimerTask {
    public void run(){
        ArrayList<HandleConn> dead=new ArrayList<>();
        for(HandleConn handleConn: ServerMain.socketArrayList){
            try {
                CheckConnAlive cca=new CheckConnAlive(handleConn.bufferedWriter);
                cca.start();
                new Thread().sleep(1500);
                if(!cca.success)
                    dead.add(handleConn);
            }catch (Exception e){
                dead.add(handleConn);
            }
        }
        for(HandleConn d:dead){
            ServerMain.killConn(d);
        }
        dead=null;
        System.gc();
    }
}
