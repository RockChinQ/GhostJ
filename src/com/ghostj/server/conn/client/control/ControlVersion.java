package com.ghostj.server.conn.client.control;

import com.ghostj.server.conn.client.HandleClient;
import com.ghostj.server.core.ServerMain;
import com.ghostj.server.log.Log;

/**
 * 告知服务器client的版本
 * @author Rock Chin
 */
public class ControlVersion implements IControlMsg{
	@Override
	public String getName() {
		return "!version";
	}

	@Override
	public void call(String[] args, String fullCmd, HandleClient client) {
		if(args.length<2){
			ServerMain.log.puts(Log.NOTIFICATION|Log.ERROR,"%CLASS%:"+client.hostName,"客户端提供了不正确的版本号信息",client);
			return;
		}
		client.ver=args[1];
		ServerMain.log.puts(Log.NOTIFICATION,"%CLASS%:"+client.hostName,"版本号:"+client.ver);
	}
}
