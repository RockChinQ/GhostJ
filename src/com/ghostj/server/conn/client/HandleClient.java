package com.ghostj.server.conn.client;

import com.ghostj.server.conn.AbstractConnHandler;
import com.ghostj.server.conn.client.controlMsg.AbstractControlMsg;
import com.ghostj.server.core.ServerMain;
import com.ghostj.server.log.Log;

import java.nio.charset.StandardCharsets;

public class HandleClient extends AbstractConnHandler {
	public long installTime=0,launchTime=0,connTime=0;
	public String hostName="",rescueName="";
	public String ver="c";

	public boolean identified=false;
	public boolean testSuccess=false;
	public long receiveAliveMsgTime=0;
	@Override
	public void run() {
		try{
			byte[] msgByte=new byte[8192];
			int len=0;
			while ((len=getInputStream().read(msgByte))!=-1){
				//检查是否是控制消息
				String strmsg=new String(msgByte, StandardCharsets.UTF_8);
				String[] cmd=strmsg.replaceAll(String.valueOf('\u0000'),"").split(" ");
				AbstractControlMsg controlMsg;
				if(strmsg.startsWith("!")
						&&(controlMsg=ServerMain.controlMsgManager.indexByName(cmd[0]))!=null){
					controlMsg.call(cmd,this);
				}else {
					//TODO 输出消息
				}
			}
		}catch (Exception readingMsg){
			ServerMain.log.puts(Log.ERROR,"%CLASS%","接收来自Client的信息时出现异常:"+ServerMain.getErrorInfo(readingMsg));
		}
		dispose();
	}
	/**
	 * 删除此连接
	 * 需要确保所有连接对象已关闭，之后停止此线程，并在HandleStorage中删除引用
	 * 调用此方法 一定 会使连接被关闭，此方法内忽略所有异常
	 */
	@Override
	public void dispose(){
		try {
			super.disposeIgnoreException();
			ServerMain.handlerStorage.clients.remove(this);
			this.stop();
		}catch (Exception ignored){}
	}
}
