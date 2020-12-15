package com.ghostj.server.log;

import com.ghostj.server.conn.client.HandleClient;
import com.ghostj.server.conn.master.HandleMaster;
import com.ghostj.server.core.ServerMain;

import java.util.ArrayList;

/**
 * 日志系统
 * 计划：支持消息类型标记
 * @author Rock Chin
 */
public class Log{
	public static final int INFORMATION=0x1,NOTIFICATION=0x2,WARNING=0x4,ERROR=0x8;
	public static final String[] msgType =new String[]{"INFO","NOTI","WARN","ERR"};

	public String prompt="GhostJ>";
	public static class LogEntity{
		private int type=0;
		private String content;
		public LogEntity(int type,String content){
			this.type=type;
			this.content=content;
		}
		public int getType() {
			return type;
		}
		public String getContent() {
			return content;
		}
	}
	ArrayList<LogEntity> logEntities=new ArrayList<>();
	//输出一个指定type的消息
	public String puts(int type,String content){
		// " + "我滴个颜什" + "\033[0m"
		String msg="\033[0m\033[200D["+getSub(type)+"]"+content+"\n\033[32m"+prompt;
		System.out.print(msg);
		return msg;
//		System.out.println("["+getSub(type)+"]"+content);
	}

	/**
	 * 重载puts方法实现带有主语的日志
	 * @param type
	 * @param content
	 * @param sub
	 * @return 输出的消息
	 */
	public String puts(int type,String sub,String content){
		//替换类名或包名转义字符
		String realSub=sub;
		if(sub.contains("%CLASS%")){
			StackTraceElement[] ste=Thread.currentThread().getStackTrace();
			StackTraceElement a=(StackTraceElement)ste[2];
			String[] spt=a.getClassName().split("[.]");
			realSub=realSub.replace("%CLASS%",spt[spt.length-2]+"."+spt[spt.length-1]);
		}
		return putWithSub(type, content, realSub);
	}

	/**
	 * 重载puts方法以实现针对指定master的输出
	 * 专用于client的消息发送到映射的master
	 * @param type
	 * @param sub
	 * @param content
	 * @param mappedClient
	 * @return 输出的消息
	 */
	public String puts(int type, String sub, String content, HandleClient mappedClient){
		String msg=puts(type, sub, content);
		//发送到所有以映射给此client的master
		ServerMain.handlerStorage.writeMasterIndexedByTag("mappedClient",mappedClient.uid+"",msg);
		return msg;
	}
	private String putWithSub(int type,String content,String sub){
		return puts(type,"<"+sub+">"+content);
	}
	public String getSub(int type){
		StringBuffer result=new StringBuffer();
		if((type&INFORMATION)!=0){
			result.append("INFO|");
		}
		if((type&NOTIFICATION)!=0){
			result.append("NOTI|");
		}
		if((type&WARNING)!=0){
			result.append("WARN|");
		}
		if((type&ERROR)!=0){
			result.append("ERROR|");
		}
		if (result.toString().endsWith("|")){
			return result.toString().substring(0,result.length()-1);
		}else
			return result.toString();
	}
}
