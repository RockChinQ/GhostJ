package com.ghostj.server;

import com.ghostj.util.Out;

import java.util.ArrayList;

public class CheckAliveMaster extends Thread{
	int time=1500;
	CheckAliveMaster(int time){
		this.time=time;
	}
	public void run(){
		ServerMain.manuallyTestConn=true;
		Out.say("CheckAliveMaster","正在测试所有连接..");

		ArrayList<HandleConn> dead = new ArrayList<>();
		for (HandleConn handleConn : ServerMain.socketArrayList) {
			System.out.print(handleConn.hostName+":");
			try {
				handleConn.success = false;
				CheckConnAlive cca = new CheckConnAlive(handleConn.bufferedWriter);
				cca.start();
				new Thread().sleep(time);
				if (!handleConn.success) {
					System.out.print("Failed\n");
					dead.add(handleConn);
				}else{
					System.out.print("Succeeded\n");
				}
			} catch (Exception e) {
				System.out.print("Failed\n");
				dead.add(handleConn);
			}
		}

		Out.say("CheckAliveMaster","检查到"+dead.size()+"个失效连接");
		for (HandleConn d : dead) {
			ServerMain.killConn(d);
		}
		dead = null;
		System.gc();
		Out.say("CheckAliveMaster","已自动清除");
		ServerMain.manuallyTestConn=false;
	}
}
