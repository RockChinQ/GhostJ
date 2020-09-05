package com.ghostj.client_old;

import java.io.BufferedReader;

public class CmdError extends Thread{
    BufferedReader errReader=null;
    public CmdError(BufferedReader errReader){
        this.errReader=errReader;
    }
    public void run(){
        try{
            String line=null;
            while (true){
                line=errReader.readLine();
                ClientMain.bufferedWriter.write(line);
                //ClientMain.bufferedWriter.newLine();
                ClientMain.bufferedWriter.flush();
            }
        }catch (Exception e){

        }
    }
}
