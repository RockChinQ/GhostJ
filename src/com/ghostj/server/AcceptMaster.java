package com.ghostj.server;

import com.ghostj.util.Out;

import java.net.ServerSocket;
import java.net.Socket;

public class AcceptMaster extends Thread{
	@Override
	public void run() {
		try {
			ServerSocket serverSocket=new ServerSocket(ServerMain.masterPort);
			while (true) {
				Socket socket=serverSocket.accept();
				Out.say("AcceptMaster","正在接受Master的连接");
				ServerMain.handleMaster=new HandleMaster(socket);
				ServerMain.handleMaster.start();
				Out.say("AcceptMaster","Master连接已准备就绪");
			}
		}catch (Exception e){
			Out.say("AcceptMaster","建立与Master的连接失败");
			e.printStackTrace();
		}
	}
}
