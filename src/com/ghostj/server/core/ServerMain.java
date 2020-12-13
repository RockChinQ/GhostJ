package com.ghostj.server.core;

import com.ghostj.server.conn.HandlerStorage;
import com.ghostj.server.conn.client.AcceptClient;
import com.ghostj.server.conn.client.control.ControlMsgManager;
import com.ghostj.server.log.Log;
import com.ghostj.util.Config;
import com.rft.core.server.BufferedFileReceiver;
import com.rft.core.server.FileServer;
import com.rft.core.server.ParallelFileServer;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ServerMain {
	public static Config config=new Config("ghostjs.ini");
	public static Log log=new Log();
	public static HandlerStorage handlerStorage=new HandlerStorage();
	public static ControlMsgManager controlMsgManager=new ControlMsgManager();
	public static AcceptClient acceptClient=new AcceptClient(config.getIntAnyhow("port",1033));

	//fileserver
	public static BufferedFileReceiver fileReceiver;
	public static FileServer fileServer;
	public static void main(String[] args) {
		try{
			log.puts(Log.INFORMATION|Log.NOTIFICATION,"%CLASS%","正在启动服务端.");
			//注册接收client控制消息的处理方法
			log.puts(Log.NOTIFICATION|Log.INFORMATION,"%CLASS%","注册客户端控制消息处理方法.");
			controlMsgManager.registerGhostJClientToServerControlProcessor();
			//启动客户端连接监听器
			log.puts(Log.NOTIFICATION|Log.INFORMATION,"%CLASS%","启动客户端连接监听器 Port:"+acceptClient.port);
			acceptClient.start();
			//启动文件服务器
			fileReceiver=new BufferedFileReceiver();
			fileReceiver.setRootPath("");
			fileServer=new ParallelFileServer(config.getIntAnyhow("rft.port",1035),fileReceiver);
			fileServer.setTaskEvent(new FileReceiveEvent());
			try {
				fileServer.start();
				log.puts(Log.NOTIFICATION|Log.INFORMATION,"%CLASS%","文件服务器已启动 port:"+config.getIntAnyhow("rft.port",1035));
			} catch (Exception e) {
				log.puts(Log.ERROR,"%CLASS%","文件服务器启动失败");
				throw e;
			}
		}catch (Exception launching){
			log.puts(Log.ERROR,"%CLASS%","启动服务端失败.\n"+getErrorInfo(launching));
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
