package com.ghostj.server;

import java.io.BufferedWriter;

public class CheckConnAlive extends Thread{
    BufferedWriter bufferedWriter=null;
    public boolean success=false;
    public CheckConnAlive(BufferedWriter bufferedWriter){
        this.bufferedWriter=bufferedWriter;
    }
    public void run(){
        try {
            bufferedWriter.write("#alives#");
            bufferedWriter.newLine();
            bufferedWriter.flush();
            //success=true;
        }catch (Exception ignored){

        }
    }
}
