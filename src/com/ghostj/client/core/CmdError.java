package com.ghostj.client.core;
import java.io.BufferedReader;

public class CmdError extends Thread{
    BufferedReader errReader;
    // FIXME: 2020/9/6 不要使用readline
    public CmdError(BufferedReader errReader){
        this.errReader=errReader;
    }
    public void run(){
        try{
            String line;
            while (true){
                line=errReader.readLine();
                HandleConn.writeToServer(line);
            }
        }catch (Exception e){}
    }
}
