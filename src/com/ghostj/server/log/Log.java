package com.ghostj.server.log;

import java.util.ArrayList;

/**
 * 日志系统
 * 计划：支持消息类型标记
 * @author Rock Chin
 */
public class Log{
	public static final int INFORMATION=0x1,NOTIFICATION=0x2,WARNING=0x4,ERROR=0x8;
	public static final String[] msgType =new String[]{"INFO","NOTI","WARN","ERR"};
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
	public void puts(int type,String content){
		System.out.println("["+getSub(type)+"]"+content);
	}

	/**
	 * 重载puts方法实现带有主语的日志
	 * @param type
	 * @param content
	 * @param sub
	 */
	public void puts(int type,String sub,String content){
		//替换类名或包名转义字符
		String realSub=sub;
		if(sub.equalsIgnoreCase("%CLASS%")){
			StackTraceElement[] ste=Thread.currentThread().getStackTrace();
			StackTraceElement a=(StackTraceElement)ste[2];
			realSub=a.getClassName();
		}
		putWithSub(type, content, realSub);
	}
	private void putWithSub(int type,String content,String sub){
		puts(type,"<"+sub+">"+content);
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
