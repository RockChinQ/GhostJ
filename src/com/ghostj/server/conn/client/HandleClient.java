package com.ghostj.server.conn.client;

import com.ghostj.server.conn.AbstractConnHandler;
import com.ghostj.server.conn.client.control.IControlMsg;
import com.ghostj.server.conn.master.HandleMaster;
import com.ghostj.server.core.ServerMain;
import com.ghostj.server.log.Log;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class HandleClient extends AbstractConnHandler {
	public static int UIDIndex=0;
	public int uid=++UIDIndex;

	public long installTime=0,launchTime=0,connTime=0;
	public String hostName="",rescueName="";
	public String ver="c";

	public boolean identified=false;
	public boolean testSuccess=false;//心跳数据检测标记
	public long receiveAliveMsgTime=0;
	//映射给此client的master
	public ArrayList<HandleMaster> mappedMaster=new ArrayList<>();

	//bufferedWriter用于与客户端对称通信
	BufferedWriter bufferedWriter;
	@Override
	public void initIOStream(Socket socket)throws Exception{
		super.initIOStream(socket);
		this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(this.getOutputStream()));
	}
	@Override
	public void run() {
		try{
			this.hostName=getSocket().getInetAddress()+":"+getSocket().getPort();
			byte[] msgByte=new byte[8192];
			int len=0;
			while ((len=getInputStream().read(msgByte))!=-1){
				//检查是否是控制消息
				String strmsg=new String(msgByte, StandardCharsets.UTF_8)
						.replaceAll(String.valueOf('\u0000'),"");
				//删除结尾的!
				if(strmsg.endsWith("!")){
					strmsg=strmsg.substring(0,strmsg.length()-1);
				}
				String[] cmd=strmsg.split(" ");
				IControlMsg controlMsg;
				if(strmsg.startsWith("!")
						&&(controlMsg=ServerMain.controlMsgManager.indexByName(cmd[0]))!=null){
					controlMsg.call(cmd,strmsg,this);
				}else {
					ServerMain.log.puts(Log.INFORMATION,"%CLASS%-"+hostName,strmsg,this);
				}
				msgByte=new byte[8192];
			}
		}catch (Exception readingMsg){
			ServerMain.log.puts(Log.ERROR,"%CLASS%","接收来自Client的信息时出现异常:"+ServerMain.getErrorInfo(readingMsg),this);
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
			ServerMain.log.puts(Log.NOTIFICATION,"%CLASS%-"+hostName,"连接已断开.",this);
			ServerMain.handlerStorage.clients.remove(this);
			this.stop();
		}catch (Exception ignored){}
	}
	@Override
	public void writeUTF(String str)throws Exception{
		this.bufferedWriter.write(str);
		this.bufferedWriter.newLine();
		this.bufferedWriter.flush();
	}
}