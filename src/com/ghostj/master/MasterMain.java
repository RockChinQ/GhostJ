package com.ghostj.master;

import com.ghostj.master.conn.HandleConn;
import com.ghostj.master.gui.InitGUI;
import com.ghostj.master.util.Config;

import java.io.BufferedWriter;
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
	public static Config config=new Config("gmaster.ini");
	public static HandleConn handleConn=null;
	int receivePeriod=0;

	public static final int bufferedTextLineAmount=100;
	public static void main(String [] args){
		initGUI=new InitGUI();
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

	}
}
