package com.ghostj.server;

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
    }
    public static void sayThisLine(String msg){
        System.out.print(msg);
    }
}
