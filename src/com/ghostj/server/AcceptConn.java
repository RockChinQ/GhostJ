package com.ghostj.server;

import com.alibaba.fastjson.JSONObject;
import com.ghostj.util.FileRW;
import com.ghostj.util.Out;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class AcceptConn extends Thread{
    static long rti=0;
    //banned ip addresses
    static ArrayList<String> banList=new ArrayList<>();
    static HashMap<String,String> description=new HashMap<>();
    public void run(){
        loadBanList();
        loadDescription();
        try{
            ServerSocket serverSocket=new ServerSocket(ServerMain.port);
            while (true){
                Socket socket=serverSocket.accept();//接受连接
                if(isBanned(String.valueOf(socket.getInetAddress()))){
                    continue;
                }
                HandleConn handleConn=new HandleConn(socket);
                handleConn.rtIndex=++rti;
                ServerMain.socketArrayList.add(handleConn);
                handleConn.start();
                if(ServerMain.focusedConn==null){
                    ServerMain.focusedConn=handleConn;
                    Out.say("AcceptConn","自动聚焦到新连接");
                }
            }
        }catch(Exception e){
            Out.say("AcceptConn","接受连接出错");
            e.printStackTrace();
        }
    }
    public static void loadBanList(){
        banList.clear();
        if(new File("banIps.txt").exists()){
            String listStr[]= FileRW.read("banIps.txt").split(";");
            for(String ip:listStr){
                banList.add(ip);
            }
        }
    }
    public static String getBannedIpsStr(){
        StringBuffer str=new StringBuffer();
        for(String ip:banList){
            str.append(ip+";");
        }
        return str.toString();
    }
    public static boolean isBanned(String ip){
        for(String ips:banList){
            if(ips.equalsIgnoreCase(ip)){
                return true;
            }
        }
        return false;
    }
    public static void loadDescription(){
        description.clear();
        if(new File("description.json").exists()){
            JSONObject descri=JSONObject.parseObject(FileRW.read("description.json"));
            for(String key: descri.keySet()){
                description.put(key,descri.getString(key));
            }
        }
    }
    public static void saveDescription(){
        JSONObject fileJson=new JSONObject();
        for(String key:description.keySet()){
            fileJson.put(key,description.get(key));
        }
        FileRW.write("description.json",fileJson.toString());
    }
}
