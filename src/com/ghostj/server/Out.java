package com.ghostj.server;

public class Out {
    public static void say(String msg){
        System.out.println(msg);
    }
    public static void say(String sub,String msg){
        say("["+sub+"]"+msg);
    }
    //在此行输出
    public static void sayThisLine(char msg){
        System.out.print(msg);
    }
    public static void sayThisLine(String msg){
        System.out.print(msg);
    }
}
