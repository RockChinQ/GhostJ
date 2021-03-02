package com.ghostj.server_old;

import com.ghostj.util.Out;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class AcceptMaster extends Thread{
	//boolean acceptable=true;

	public static ArrayList<HandleMaster> masters=new ArrayList<>();
	@Override
	public void run() {
		try {
			ServerSocket serverSocket=new ServerSocket(ServerMain.masterPort);
			while (true) {
				Socket socket=serverSocket.accept();
				if(AcceptConn.isBanned(String.valueOf(socket.getInetAddress()))){
					try{socket.close();}catch (Exception ignored){}
					continue;
				}
				new Thread(()-> {
					try {
						Out.say("AcceptMaster","正在接受Master的连接("+socket.getInetAddress()+")");
						HandleMaster handleMaster = new HandleMaster(socket);
						handleMaster.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "GBK"));
						handleMaster.outputStreamWriter = new OutputStreamWriter(socket.getOutputStream(), "GBK");
						masters.add(handleMaster);
						handleMaster.start();
					}catch (Exception e){
						Out.say("AcceptMaster-annoyThr","无法接受master连接");
					}
				}).start();
			}
		}catch (Exception e){
			Out.say("AcceptMaster","建立与Master的连接失败");
			e.printStackTrace();
		}
	}
	public static boolean masterOnline(){
		return masters.size()!=0;
	}
}
