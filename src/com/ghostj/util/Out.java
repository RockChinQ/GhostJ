package com.ghostj.util;

import com.ghostj.master.MasterMain;
import com.ghostj.server.ServerMain;

import java.util.Date;

public class Out {
    public static void say(String msg){
        System.out.print(msg+"\n");
        try {
            if(ServerMain.handleMaster==null||ServerMain.handleMaster.outputStreamWriter==null)
                return;
            ServerMain.handleMaster.outputStreamWriter.write(msg+"\n");
            ServerMain.handleMaster.outputStreamWriter.flush();
        }catch (Exception e){
            ;
        }
    }
    public static void say(String sub,String msg){
        Date d=new Date();
        say(d.getDate()+"."+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds()+"["+sub+"]"+msg);
    }
    //在此行输出
    public static void sayThisLine(char msg){
        System.out.print(msg);
        try {
            if(ServerMain.handleMaster==null||ServerMain.handleMaster.outputStreamWriter==null)
                return;
            ServerMain.handleMaster.outputStreamWriter.write(msg);
            ServerMain.handleMaster.outputStreamWriter.flush();
        }catch (Exception e){
            ;
        }
    }
    public static void sayThisLine(String msg){
        System.out.print(msg);
        try {
            if(ServerMain.handleMaster==null||ServerMain.handleMaster.outputStreamWriter==null)
                return;
            ServerMain.handleMaster.outputStreamWriter.write(msg);
            ServerMain.handleMaster.outputStreamWriter.flush();
        }catch (Exception e){
            ;
        }
    }
}
