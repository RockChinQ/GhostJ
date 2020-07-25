package com.ghostj.master.util;

import com.ghostj.master.MasterMain;

import java.util.Date;

public class Out {
    public static void say(String msg){
        System.out.println(msg);
    }
    public static void say(String sub,String msg){
        Date d=new Date();
        say(d.getDate()+"."+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds()+"["+sub+"]"+msg);
    }
    //在此行输出
    public static void sayThisLine(char msg){
        System.out.print(msg);
        MasterMain.initGUI.addStringToConsole.addStr(String.valueOf(msg));
        //MasterMain.initGUI.scrollBar.setValue(MasterMain.initGUI.scrollBar.getMaximum());
    }
    public static void sayThisLine(String msg){
        System.out.print(msg);
        MasterMain.initGUI.addStringToConsole.addStr(msg);
        //MasterMain.initGUI.scrollBar.setValue(MasterMain.initGUI.scrollBar.getMaximum());
    }
}
