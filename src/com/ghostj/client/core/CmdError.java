package com.ghostj.client.core;
import com.ghostj.client.conn.HandleConn;
import com.ghostj.client.func.FuncDefault;
import com.ghostj.client.func.FuncProcess;

import java.io.BufferedReader;

/**
 * 获取命令行输出的异常并输出
 * @author Rock Chin
 */
public class CmdError extends Thread{
    BufferedReader errReader;
    ProcessCmd processCmd;
    StringBuffer buffer=new StringBuffer("");
    public CmdError(BufferedReader errReader,ProcessCmd processCmd){
        this.errReader=errReader;
        this.processCmd=processCmd;
    }
    public void run(){
        try{
            String line;
            while (true){
                line=errReader.readLine();
                if(FuncDefault.amIFocused(processCmd))
                    HandleConn.writeToServer(line);
                else{
                    buffer.append(line);
                }
            }
        }catch (Exception e){}
    }
    public void flush() {
        try {
            HandleConn.writeToServer(buffer.toString());
        }catch (Exception ignored){}
        buffer=new StringBuffer();
    }
}
