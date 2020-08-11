package com.ghostj.util;

import com.ghostj.master.MasterMain;
import com.ghostj.server.AcceptMaster;
import com.ghostj.server.HandleMaster;
import com.ghostj.server.ServerMain;

import java.util.Date;

public class Out {
    public static boolean isPromptEnd=false;
    public static void say(String msg){
        System.out.print((isPromptEnd?"\n":"")+msg+"\n");
        try {
            /*if(!ServerMain.handleMaster.available || ServerMain.handleMaster.outputStreamWriter == null)
                return;*/
            for(HandleMaster master: AcceptMaster.masters) {
                master.sentMsg((isPromptEnd ? "\n" : "") + msg + "\n");
            }
        }catch (Exception e){
            ;
        }
        isPromptEnd=false;
    }
    public static void say(String sub,String msg){
        Date d=new Date();
        say(d.getDate()+"."+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds()+"["+sub+"]"+msg);
    }
    public static void putPrompt(){
        if(ServerMain.focusedConn!=null)
            sayThisLine("@server|client:"+ServerMain.focusedConn.hostName+">");
        else
            sayThisLine("@server>");
        isPromptEnd=true;
    }
    //在此行输出
    public static void sayThisLine(char msg){

        System.out.print((isPromptEnd?"\n":"")+msg);
        try {
           /* if(!ServerMain.handleMaster.available || ServerMain.handleMaster.outputStreamWriter == null)
                return;*/
            for(HandleMaster master: AcceptMaster.masters) {
                master.sentMsg((isPromptEnd ? "\n" : "") + msg + "\n");
            }
        }catch (Exception e){
            ;
        }
        isPromptEnd=false;
    }
    public static void sayThisLine(String msg){
        System.out.print((isPromptEnd?"\n":"")+msg);
        try {
           /* if(!ServerMain.handleMaster.available || ServerMain.handleMaster.outputStreamWriter == null)
                return;*/
            for(HandleMaster master: AcceptMaster.masters) {
                master.sentMsg((isPromptEnd ? "\n" : "") + msg + "\n");
            }
        }catch (Exception e){
            ;
        }
        isPromptEnd=false;
    }
}
