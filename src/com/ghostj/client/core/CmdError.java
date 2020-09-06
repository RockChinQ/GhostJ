package com.ghostj.client.core;
import com.ghostj.client.conn.HandleConn;

import java.io.BufferedReader;

/**
 * 获取命令行输出的异常并输出
 * @author Rock Chin
 */
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
