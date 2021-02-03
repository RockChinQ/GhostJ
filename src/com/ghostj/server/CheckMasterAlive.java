package com.ghostj.server;

import com.ghostj.util.Out;

import java.util.ArrayList;
import java.util.TimerTask;

public class CheckMasterAlive extends TimerTask {
	public boolean alive=false;
	@Override
	public void run() {
		try{
			if(AcceptMaster.masterOnline()){//如果有master在线
				//挨个测试所有master
				ArrayList<HandleMaster> masterTokill=new ArrayList<>();
				for(HandleMaster handleMaster:AcceptMaster.masters){
					handleMaster.alive=false;
					new Thread( ()->{
						try {
							handleMaster.outputStreamWriter.write("!alivem!");
							handleMaster.outputStreamWriter.flush();
						} catch (Exception e) {
//							kill(handleMaster);
							masterTokill.add(handleMaster);
						}
					}).start();
					Thread.sleep(10000);
					if (!alive)
//						kill();
						masterTokill.add(handleMaster);
					else {
						if(AcceptMaster.masterOnline())
							ServerMain.tagLog.addTag(".Master","alive");
					}
				}
				masterTokill.forEach(CheckMasterAlive::kill);
			}
		}catch (Exception ignored){
		}
	}
	public static void kill(HandleMaster master){
		try{
			try{
				master.outputStreamWriter.write("!relogin!");
				master.outputStreamWriter.flush();
			}catch (Exception e){

			}
			AcceptMaster.masters.remove(master);
			ServerMain.sendMasterList();
			master.outputStreamWriter.close();
			master.socket.close();
			master.stop();
			Out.say("CheckMasterAlive","Master已断连，正在重置连接");
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
