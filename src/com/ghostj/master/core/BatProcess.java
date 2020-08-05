package com.ghostj.master.core;

public class BatProcess {
	public static void processBatch(String batStr,String[] args)throws Exception{
		//替换所有参数引用
		String bat=batStr;
		for(int i=0;i<args.length;i++){
			bat=new String(bat.toString().replaceAll("%"+i+"%",args[i]));
		}
		String batLn[]=bat.toString().split("\n");
		//解析出运行此bat的对象
		//#!master或#!client
		if(batLn[0].equals("#!master")){
			masterProcess(batLn);
		}else if(batLn[0].equals("#!client")){

		}
	}
	private static void masterProcess(String[] cmds){

	}
}
