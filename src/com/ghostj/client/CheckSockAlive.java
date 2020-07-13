package com.ghostj.client;

public class CheckSockAlive extends Thread{
    public boolean success=false;
    @Override
    public void run() {
        try {
            ClientMain.bufferedWriter.write("!alive");
            ClientMain.bufferedWriter.flush();
            success=true;
        }catch (Exception e){
            e.printStackTrace();
            //success=true;
        }
    }
}
