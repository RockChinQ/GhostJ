package com.ghostj.server.conn.client.control;

import com.ghostj.server.conn.client.HandleClient;

/**
 * 描述Server能识别的来自Client的一个控制消息
 * @author Rock Chin
 */
public interface IControlMsg {
	/**
	 * 控制消息的名称是必需的
	 * @return
	 */
	String getName();

	/**
	 * 调用此控制指令
	 * @param args
	 */
	void call(String[] args,String fullCmd, HandleClient client);
}
