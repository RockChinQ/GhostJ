package com.ghostj.master.conn;

import com.ghostj.master.MasterMain;
import com.ghostj.util.Out;

import java.util.TimerTask;

public class CheckServerAlive extends TimerTask {
	boolean alive=false;
	@Override
	public void run() {
		try{
			if(MasterMain.socket!=null) {
				alive = false;
				try {
					//Out.say("CheckServerAlive","检测server连接");
					MasterMain.bufferedWriter.write("#alivems#");
					MasterMain.bufferedWriter.newLine();
					MasterMain.bufferedWriter.flush();
				} catch (Exception e) {
					MasterMain.handleConn.kill("server已断连");
					e.printStackTrace();
				}
				new Thread().sleep(30000);
				if(!alive)
					MasterMain.handleConn.kill("server已断连");
			}
		}catch (Exception e){
			Out.say("CheckServerAlive","检测连接时出现错误");
			e.printStackTrace();
		}
	}

}
