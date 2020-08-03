package com.ghostj.server;

import com.ghostj.util.Out;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class AcceptMaster extends Thread{
	boolean acceptable=true;
	@Override
	public void run() {
		try {
			ServerSocket serverSocket=new ServerSocket(ServerMain.masterPort);
			while (true) {
				if (!acceptable){
					try{
						new Thread().sleep(5000);
					}catch (Exception e){}
					continue;
				}
				Socket socket=serverSocket.accept();
				Out.say("AcceptMaster","正在接受Master的连接");
				ServerMain.handleMaster=new HandleMaster(socket);
				ServerMain.handleMaster.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream(),"GBK"));
				ServerMain.handleMaster.outputStreamWriter=new OutputStreamWriter(socket.getOutputStream(),"GBK");

				ServerMain.handleMaster.start();
				acceptable=false;
				Out.say("AcceptMaster","Master连接已准备就绪");
			}
		}catch (Exception e){
			Out.say("AcceptMaster","建立与Master的连接失败");
			e.printStackTrace();
		}
	}
}
