package com.ghostj.server.conn.client.control;

import com.ghostj.server.conn.client.HandleClient;
import com.ghostj.server.core.ServerMain;
import com.ghostj.server.log.Log;

public class ControlSendPicURL implements IControlMsg{
	@Override
	public String getName() {
		return "!sendpicurl";
	}

	@Override
	public void call(String[] args, String fullCmd, HandleClient client) {
//		ServerMain.log.puts(Log.INFORMATION,"%CLASS%-"+client.hostName,"获取到新截图,url:http://39.100.5.139/ghost/"+ ServerMain.fileReceiver.getRootPath()+args[1],client);

		ServerMain.log.puts(Log.NOTIFICATION|Log.INFORMATION,"%CLASS%"
				, "获取到新截图,url:http://39.100.5.139/ghost/"+ ServerMain.fileReceiver.getRootPath()+args[1],client);
//		com.ghostj.server_old.ServerMain.sendToSpecificMaster("!scrd http://39.100.5.139/ghost/" + ServerMain.fileReceiver.getRootPath() + fileInfo.getSavePath() + "/" + fileInfo.getName() + "\n", "screenShot");

	}
}
