package com.ghostj.client.conn;

import com.ghostj.client.util.Out;

import java.util.TimerTask;

/**
 * 向服务端发送心跳数据以检测连接是否正常
 * @author Rock Chin
 */
public class CheckAliveTimer extends TimerTask {
    private static boolean alive=false;

    public static boolean isAlive() {
        return alive;
    }

    public static void setAlive(boolean alive) {
        CheckAliveTimer.alive = alive;
    }

    @Override
    public void run() {
        try {
            Out.say("CheckAliveTimer","check alive");
            alive=false;
            Thread send=new Thread(()->{
                HandleConn.writeToServerIgnoreException("!alive!");
            });
            send.start();
            Thread.sleep(5000);
            Out.say("CheckAliveTimer",alive+"");
            if(!alive){
                HandleConn.closeSocket();
            }
        }catch (Exception e){

        }
    }
}
