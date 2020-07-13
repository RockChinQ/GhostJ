package com.ghostj.client;

import java.util.TimerTask;

public class CheckAliveTimer extends TimerTask {
    @Override
    public void run() {
        try {
            //Out.say("CheckAliveTimer","check alive");
            CheckSockAlive checkSockAlive = new CheckSockAlive();
            checkSockAlive.start();
            new Thread().sleep(1500);
            if(!checkSockAlive.success){
                ClientMain.socket.close();
            }
        }catch (Exception e){

        }
    }
}
