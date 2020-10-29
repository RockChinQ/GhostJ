package com.ghostj.master.core;

import com.ghostj.master.MasterMain;
import com.ghostj.master.util.Out;

import java.util.ArrayList;
import java.util.Arrays;

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
		if(batLn[0].equals("#!master")){//master级别的批处理
			BatProcess.cmds.addAll(Arrays.asList(batLn).subList(1, batLn.length));
			masterProcess();
		}else if(batLn[0].equals("#!client")){//client级别的批处理
			//直接清空client的batch文件并挨行写入批处理文件
			/*StringBuffer batch=new StringBuffer();
			for (String ln:Arrays.asList(batLn).subList(1, batLn.length)){
				batch.append(ln+"\n");
			}*/
			int option=javax.swing.JOptionPane.showConfirmDialog(MasterMain.initGUI.mainwd,"即将清空客户端的批处理文件，写入以下内容并执行，确认？\n"+Arrays.asList(batLn).subList(1, batLn.length));
			System.out.println(option);
			if(option==0){
				new Thread(()->{
					try {
						MasterMain.bufferedWriter.write("!!bat reset");
						MasterMain.bufferedWriter.newLine();
						MasterMain.bufferedWriter.flush();

						for (String ln : Arrays.asList(batLn).subList(1, batLn.length)) {
							MasterMain.bufferedWriter.write("!!bat add " + ln);
							MasterMain.bufferedWriter.newLine();
							MasterMain.bufferedWriter.flush();
						}
						MasterMain.bufferedWriter.write("!!bat run");
						MasterMain.bufferedWriter.newLine();
						MasterMain.bufferedWriter.flush();
						Out.say("BatProcess", "已发送客户端侧批处理");
					}catch (Exception e){
						e.printStackTrace();
					}
			}).start();
			}
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
