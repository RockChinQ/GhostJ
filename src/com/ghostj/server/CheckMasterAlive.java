package com.ghostj.server;

import com.ghostj.util.Out;

import java.util.TimerTask;

public class CheckMasterAlive extends TimerTask {
	public boolean alive=false;
	@Override
	public void run() {
		try{
			if(!ServerMain.acceptMaster.acceptable) {
				alive = false;
				new Thread() {{
					try {
						ServerMain.handleMaster.outputStreamWriter.write("!alivem!");
						ServerMain.handleMaster.outputStreamWriter.flush();
					} catch (Exception e) {
						kill();
					}
				}}.start();
				new Thread().sleep(1000);
				if (!alive)
					kill();
			}
		}catch (Exception e){
			e.printStackTrace();
			Out.say("CheckMasterAlive","检测master连接时出现错误");
		}
	}
	public void kill(){
		try{
			try{
				ServerMain.handleMaster.outputStreamWriter.write("!relogin!");
				ServerMain.handleMaster.outputStreamWriter.flush();
			}catch (Exception e){

			}
			ServerMain.handleMaster.outputStreamWriter.close();
			ServerMain.handleMaster.socket.close();
			ServerMain.handleMaster.stop();
			ServerMain.acceptMaster.acceptable=true;
			Out.say("CheckMasterAlive","Master已断连，正在重置连接");
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
