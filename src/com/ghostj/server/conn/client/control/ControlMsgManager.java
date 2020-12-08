package com.ghostj.server.conn.client.control;

import java.util.ArrayList;

/**
 * 管理所有的控制消息
 * 便于HandleClient的调用
 * @author Rock Chin
 */
public class ControlMsgManager {
	ArrayList<IControlMsg> controlMsgArrayList=new ArrayList<>();

	/**
	 * 注册一个控制消息处理方法
	 * @param controlMsg
	 * @return
	 */
	public ControlMsgManager register(IControlMsg controlMsg){
		this.controlMsgArrayList.add(controlMsg);
		return this;
	}
	public void registerGhostJClientToServerControlProcessor(){
		register(new ControlAlive())
				.register(new ControlAlives())
				.register(new ControlFinish())
				.register(new ControlVersion());
	}
	public IControlMsg indexByName(String name){
		for(IControlMsg controlMsg:this.controlMsgArrayList){
			if (controlMsg.getName().equalsIgnoreCase(name)){
				return controlMsg;
			}
		}
		return null;
	}
}
