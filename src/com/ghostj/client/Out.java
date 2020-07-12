package com.ghostj.client;

public class Out {
    public static void say(String msg){
        System.out.println(msg);
    }
    public static void say(String sub,String msg){
        say("["+sub+"]"+msg);
    }
}
