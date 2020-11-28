package com.ghostj.util;

import com.ghostj.server_old.AcceptMaster;
import com.ghostj.server_old.HandleMaster;
import com.ghostj.server_old.ServerMain;

import java.util.Date;

public class Out {
    public static boolean isPromptEnd=false;
    public static StringBuffer history=new StringBuffer();
    public static void say(String msg){
        System.out.print(msg+"\n");
        history.append(msg+"\n");
        try {
            /*if(!ServerMain.handleMaster.available || ServerMain.handleMaster.outputStreamWriter == null)
                return;*/
            for(HandleMaster master: AcceptMaster.masters) {
                master.sentMsg( msg + "\n");
            }
        }catch (Exception e){
            ;
        }
        checkHistory();
        isPromptEnd=false;
    }
    public static void say(String sub,String msg){
        Date d=new Date();
        say((isPromptEnd ? "\n" : "") +d.getDate()+"."+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds()+"["+sub+"]"+msg);
    }
    public static void putPrompt(){
        if(ServerMain.focusedConn!=null)
            sayThisLine("@server|client:"+ServerMain.focusedConn.hostName+">");
        else
            sayThisLine("@server>");
        isPromptEnd=true;
    }

    /**
     * �������������Ϣ
     * �����ᱻ������ʷ��¼
     * @param msg
     */
    public static void noRecordSay(String msg){
        try {
           /* if(!ServerMain.handleMaster.available || ServerMain.handleMaster.outputStreamWriter == null)
                return;*/
            for(HandleMaster master: AcceptMaster.masters) {
                master.sentMsg( msg );
            }
        }catch (Exception e){
            ;
        }
    }
    //�ڴ������
    public static void sayThisLine(char msg){

        System.out.print((isPromptEnd?"\n":"")+msg);
        history.append((isPromptEnd?"\n":"")+msg);
        try {
           /* if(!ServerMain.handleMaster.available || ServerMain.handleMaster.outputStreamWriter == null)
                return;*/
            for(HandleMaster master: AcceptMaster.masters) {
                master.sentMsg((isPromptEnd ? "\n" : "") + msg );
            }
        }catch (Exception e){
            ;
        }
        checkHistory();
        isPromptEnd=false;
    }
    public static void sayThisLine(String msg){
        System.out.print((isPromptEnd?"\n":"")+msg);
        history.append((isPromptEnd?"\n":"")+msg);
        try {
           /* if(!ServerMain.handleMaster.available || ServerMain.handleMaster.outputStreamWriter == null)
                return;*/
            for(HandleMaster master: AcceptMaster.masters) {
                master.sentMsg((isPromptEnd ? "\n" : "") + msg);
            }
        }catch (Exception e){
            ;
        }
        checkHistory();
        isPromptEnd=false;
    }

    static final int HISTORY_CHAR_LEN=1000;
    public static void checkHistory(){
        if(history.length()>HISTORY_CHAR_LEN){
            history=new StringBuffer(history.substring(history.length()-HISTORY_CHAR_LEN,history.length()));
        }
    }
}
