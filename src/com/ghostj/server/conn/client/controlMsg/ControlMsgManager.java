package com.ghostj.server.conn.client.controlMsg;

import java.util.ArrayList;

/**
 * 管理所有的控制消息
 * 便于HandleClient的调用
 * @author Rock Chin
 */
public class ControlMsgManager {
	ArrayList<AbstractControlMsg> controlMsgArrayList=new ArrayList<>();

	/**
	 * 注册一个控制消息处理方法
	 * @param controlMsg
	 * @return
	 */
	public ControlMsgManager register(AbstractControlMsg controlMsg){
		this.controlMsgArrayList.add(controlMsg);
		return this;
	}
	public AbstractControlMsg indexByName(String name){
		for(AbstractControlMsg controlMsg:this.controlMsgArrayList){
			if (controlMsg.name.equalsIgnoreCase(name)){
				return controlMsg;
			}
		}
		return null;
	}
}
