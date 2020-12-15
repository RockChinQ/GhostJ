package com.ghostj.server.conn.client.control;

import com.ghostj.server.conn.client.HandleClient;
import com.ghostj.server.core.ServerMain;
import com.ghostj.server.log.Log;
import com.ghostj.util.TimeUtil;

/**
 * client登录时发送客户端信息
 */
public class ControlInfo implements IControlMsg{
	@Override
	public String getName() {
		return "!info";
	}

	@Override
	public void call(String[] args, String fullCmd, HandleClient client) {
		if(args.length<4){
			ServerMain.log.puts(Log.NOTIFICATION|Log.ERROR,"%CLASS%","客户端提供了格式不正确的info消息");
			ServerMain.log.puts(Log.NOTIFICATION|Log.ERROR,"%CLASS%","info:"+fullCmd);
			return;
		}
		client.hostName=args[1];
		client.rescueName="r"+args[1];
		client.ver=args[2];
		client.launchTime=Long.parseLong(args[3]);
		client.installTime=args.length>4?Long.parseLong(args[4]):0;
		ServerMain.log.puts(Log.NOTIFICATION,"%CLASS%-client:"+args[1]
				,"ver:"+args[2]+" start:"+args[3]
						+" install:"+ TimeUtil.millsToMMDDHHmmSS(client.installTime));
		client.identified=true;
		//TODO 发送列表给master
		//TODO 检查重命名计划
		//TODO 添加TagLog记录
	}
}
