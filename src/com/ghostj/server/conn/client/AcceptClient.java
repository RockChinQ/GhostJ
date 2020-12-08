package com.ghostj.server.conn.client;

import com.alibaba.fastjson.JSONObject;
import com.ghostj.server.core.ServerMain;
import com.ghostj.server.log.Log;
import com.ghostj.util.FileRW;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 接受客户端连接
 */
public class AcceptClient extends Thread{
	public int port=1033;
	//banned ip addresses
	static ArrayList<String> banList=new ArrayList<>();
	static HashMap<String,String> description=new HashMap<>();

	public AcceptClient(int port){
		this.port=port;
	}
	/**
	 * 死等接受连接，现在已删除聚焦概念，则不自动聚焦
	 */
	public void run(){
		loadBanList();
		loadDescription();
		try{
			ServerSocket serverSocket=new ServerSocket(port);
			while (true){
				try {
					Socket socket = serverSocket.accept();//接受连接
					if (isBanned(String.valueOf(socket.getInetAddress()))) {
						continue;
					}
					HandleClient handleClient = new HandleClient();
					handleClient.initIOStream(socket);
					ServerMain.handlerStorage.clients.add(handleClient);
					handleClient.start();
				}catch (Exception accepting){
					ServerMain.log.puts(Log.NOTIFICATION|Log.ERROR,"%CLASS%","无法接受新客户端连接\n"+ServerMain.getErrorInfo(accepting));
				}
			}
		}catch(Exception e){
			ServerMain.log.puts(Log.NOTIFICATION|Log.ERROR,"%CLASS%","无法使用监听器\n"+ServerMain.getErrorInfo(e));
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
