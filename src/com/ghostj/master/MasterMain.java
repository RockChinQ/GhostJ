package com.ghostj.master;

import com.ghostj.master.conn.HandleConn;
import com.ghostj.master.gui.InitGUI;
import com.ghostj.master.util.Config;

import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.net.Socket;

public class MasterMain {
	public static InitGUI initGUI;
	public static Socket socket=null;
	public static InputStreamReader inputStreamReader=null;
	public static BufferedWriter bufferedWriter=null;
	public static Config config=new Config("gmaster.ini");
	public static HandleConn handleConn=null;
	public static void main(String [] args){
		initGUI=new InitGUI();
	}
}
