package com.ghostj.server.conn.client.control;

import com.ghostj.server.conn.client.HandleClient;

/**
 * 客户端执行完一个批处理命令后的告知消息
 */
public class ControlFinish implements IControlMsg{
	@Override
	public String getName() {
		return "!finish";
	}

	@Override
	public void call(String[] args, String fullCmd, HandleClient client) {
		//TODO 完成
	}
}
