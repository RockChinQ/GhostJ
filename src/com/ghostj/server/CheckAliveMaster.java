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
			Out.sayThisLine(handleConn.hostName+":");
			try {
				handleConn.success = false;
				CheckConnAlive cca = new CheckConnAlive(handleConn.bufferedWriter);
				cca.start();
				new Thread().sleep(time);
				if (!handleConn.success) {
					Out.sayThisLine("Failed\n");
					dead.add(handleConn);
				}else{
					Out.sayThisLine("Succeeded\n");
				}
			} catch (Exception e) {
				Out.sayThisLine("Failed\n");
				dead.add(handleConn);
			}
		}

		Out.say("CheckAliveMaster","检查到"+dead.size()+"个失效连接");
		for (HandleConn d : dead) {
			ServerMain.killConn(d);
		}
		System.gc();
		Out.say("CheckAliveMaster","已自动清除");
		ServerMain.cmdProcessFinish();
		ServerMain.manuallyTestConn=false;
	}
}
