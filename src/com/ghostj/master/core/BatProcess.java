package com.ghostj.master.core;

import com.ghostj.master.MasterMain;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public class BatProcess {
	public static boolean processing=false;
	public static boolean finish=false;
	public static boolean destroy=false;
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
		processing=true;
		nextCmd:for (int i=0;i<cmds.length;i++){
			try {
				MasterMain.bufferedWriter.write(cmds[i]);
				MasterMain.bufferedWriter.flush();
				finish=false;
				destroy=false;
				while (true){
					if(destroy){
						processing=false;
						return;
					}
					if (finish){
						continue nextCmd;
					}
					Thread.sleep(1000);
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		processing=false;
	}
}
