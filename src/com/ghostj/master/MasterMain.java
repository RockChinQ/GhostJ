package com.ghostj.master;

import com.ghostj.master.conn.CheckServerAlive;
import com.ghostj.master.conn.HandleConn;
import com.ghostj.master.gui.InitGUI;
import com.ghostj.master.util.Config;
import com.ghostj.master.util.FileRW;
import com.ghostj.master.util.TagLog;
import com.ghostj.server.ServerMain;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MasterMain {
	public static InitGUI initGUI;
	public static Socket socket=null;
	public static InputStreamReader inputStreamReader=null;
	public static BufferedWriter bufferedWriter=null;
	public static Config config;
	public static HandleConn handleConn=null;
	int receivePeriod=0;

	public static final int bufferedTextLineAmount=100;
	public static CheckServerAlive checkServerAlive=new CheckServerAlive();

	public static TagLog tagLog=new TagLog();
	//内建服务器
	public static ServerMain internalServer;
	public static void main(String [] args){
		if(!new File("gmaster.ini").exists()){
			FileRW.write("gmaster.ini","");
		}
		config=new Config("gmaster.ini");
		initGUI=new InitGUI();

		new Timer().schedule(checkServerAlive,4000,10*60*1000);
//		new Timer().schedule(new TimerTask() {
//			@Override
//			public void run() {
//				initGUI.scrollBar.setValue(initGUI.scrollBar.getMaximum());
//			}
//		},new Date(),200);
//		new Timer().schedule(new TimerTask() {
//			@Override
//			public void run() {
//				try {
//					String text[] = initGUI.console.getText().split("\n");
//					int len = text.length;
//					if (len > bufferedTextLineAmount) {
//						System.out.println("清理" + (len - bufferedTextLineAmount) + "行 共"+len);
//						StringBuffer retext = new StringBuffer("");
//						for (int i = len - bufferedTextLineAmount; i < len; i++) {
//							retext.append("\n" + text[i]);
//						}
//						initGUI.console.setText(retext.toString());
//						initGUI.console.setCaretPosition(retext.length());
//					}
//				}catch (Exception e){
//					e.printStackTrace();
//				}
//			}
//		},new Date(),1000);
		new Timer().schedule(new TimerTask(){
			@Override
			public void run() {
				initGUI.mainwd.setTitle("total:"+Runtime.getRuntime().totalMemory()+" used:"+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
			}
		},new Date(),2000);
	}
	public static void writeToServer(String cmd){
		try {
			MasterMain.bufferedWriter.write(cmd);
			MasterMain.bufferedWriter.newLine();
			MasterMain.bufferedWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
