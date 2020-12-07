package com.ghostj.server.core;

import com.ghostj.server.conn.HandlerStorage;
import com.ghostj.server.conn.client.controlMsg.ControlMsgManager;
import com.ghostj.server.log.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ServerMain {
	public static Log log=new Log();
	public static HandlerStorage handlerStorage=new HandlerStorage();
	public static ControlMsgManager controlMsgManager=new ControlMsgManager();
	public static void main(String[] args) {
		//注册接收client控制消息的处理方法

		try{
			log.puts(Log.INFORMATION|Log.NOTIFICATION,"%CLASS%","正在启动服务端.");
		}catch (Exception launching){
			log.puts(Log.ERROR,"%CLASS%","启动服务端失败.");
		}
	}
	/**
	 * 从一个exception对象获取完整的报错信息
	 * @param e exception
	 * @return 完整异常信息
	 */
	public static String getErrorInfo(Exception e){
		StringWriter sw=new StringWriter();
		PrintWriter pw=new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString().replaceAll("\t","    ");
	}

}
