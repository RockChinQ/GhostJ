package com.ghostj.server.conn.client.controlMsg;

import com.ghostj.server.conn.client.HandleClient;

/**
 * 描述Server能识别的来自Client的一个控制消息
 * @author Rock Chin
 */
public abstract class AbstractControlMsg {
	protected String name="noname";
	public String getName() {
		return name;
	}

	/**
	 * 调用此控制指令
	 * @param args
	 */
	public abstract void call(String[] args, HandleClient client);
}
