package com.ghostj.client_old;

import java.util.TimerTask;

public class CheckAliveTimer extends TimerTask {
    @Override
    public void run() {
        try {
            Out.say("CheckAliveTimer","check alive");
            ClientMain.success=false;
            CheckSockAlive checkSockAlive = new CheckSockAlive();
            checkSockAlive.start();
            new Thread().sleep(1500);
            Out.say("CheckAliveTimer",ClientMain.success+"");
            if(!ClientMain.success){
                ClientMain.socket.close();
            }
        }catch (Exception e){

        }
    }
}
