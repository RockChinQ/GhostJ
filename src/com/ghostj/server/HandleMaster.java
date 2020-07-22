package com.ghostj.server;

import java.net.Socket;

public class HandleMaster extends Thread{
	Socket socket=null;
	public HandleMaster(Socket socket){
		this.socket=socket;
	}

	@Override
	public void run() {

	}
}
