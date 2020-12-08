package com.ghostj.server.conn.client.control;

import com.ghostj.server.conn.client.HandleClient;

/**
 * 回复客户端的心跳数据消息
 */
public class ControlAlive implements IControlMsg {
	@Override
	public String getName() {
		return "!alive";
	}

	@Override
	public void call(String[] args, String fullCmd, HandleClient client) {
		client.writeUTFIgnoreException("#alive#");
	}
}
