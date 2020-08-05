package com.ghostj.master.core;

import com.ghostj.master.MasterMain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;

public class BatProcess {
	public static ArrayList<String> cmds=new ArrayList<>();
	public static void processBatch(String batStr,String[] args)throws Exception{
		//替换所有参数引用
		String bat=batStr;
		for(int i=0;i<args.length;i++){
			bat= bat.replaceAll("%" + i + "%", args[i]);
		}
		String batLn[]= bat.split("\n");
		//解析出运行此bat的对象
		//#!master或#!client
		if(batLn[0].equals("#!master")){
			for (int i=1;i<batLn.length;i++){
				BatProcess.cmds.add(batLn[i]);
			}
			masterProcess();
		}else if(batLn[0].equals("#!client")){

		}
	}
	public static void masterProcess(){
		if(cmds.size()>0) {
			String cm = BatProcess.cmds.get(0);
			BatProcess.cmds.remove(0);
			try {
				MasterMain.bufferedWriter.write(cm);
				MasterMain.bufferedWriter.newLine();
				MasterMain.bufferedWriter.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
