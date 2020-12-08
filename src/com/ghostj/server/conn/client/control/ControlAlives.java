package com.ghostj.server.conn.client.control;

import com.ghostj.server.conn.client.HandleClient;

import java.util.Date;

/**
 * 心跳数据检测连接时记录客户端的反馈
 */
public class ControlAlives implements IControlMsg{
	@Override
	public String getName() {
		return "!alives";
	}

	@Override
	public void call(String[] args, String fullCmd, HandleClient client) {
		client.testSuccess=true;
		client.receiveAliveMsgTime=new Date().getTime();
	}
}
