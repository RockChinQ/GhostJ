package com.ghostj.master.conn;

import com.ghostj.master.MasterMain;
import com.ghostj.util.Out;

import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class CreateConn extends Thread{
	@Override
	public void run() {
		try{
			MasterMain.config.set("ip",MasterMain.initGUI.loginPanel.ipInput.getValue());
			MasterMain.config.set("port",MasterMain.initGUI.loginPanel.portInput.getValue());
			MasterMain.config.set("pw",MasterMain.initGUI.loginPanel.pwInput.getValue());
			MasterMain.config.write();
			MasterMain.initGUI.loginPanel.setTitle("Login-尝试连接");
			new Thread(() -> {
				try{
					MasterMain.socket=new Socket(MasterMain.initGUI.loginPanel.ipInput.getValue(),Integer.parseInt(MasterMain.initGUI.loginPanel.portInput.getValue()));
					MasterMain.inputStreamReader=new InputStreamReader(MasterMain.socket.getInputStream());
					MasterMain.bufferedWriter=new BufferedWriter(new OutputStreamWriter(MasterMain.socket.getOutputStream()));
					MasterMain.initGUI.loginPanel.setTitle("Login-连接成功");
					MasterMain.initGUI.mainwd.setEnabled(true);
					MasterMain.initGUI.mainwd.setVisible(true);
					MasterMain.initGUI.loginPanel.dispose();
					MasterMain.handleConn=new HandleConn();
					MasterMain.handleConn.start();
					//发送密码
					MasterMain.bufferedWriter.write("#pw "+MasterMain.initGUI.loginPanel.pwInput.getValue());
					MasterMain.bufferedWriter.newLine();
					MasterMain.bufferedWriter.flush();
					Out.say("CreateConn","已发送密码");
					MasterMain.initGUI.bgp.setVisible(true);
				}catch (Exception e){
					Out.say("CreateConn","无法建立连接");
					MasterMain.initGUI.loginPanel.setTitle("Login-连接失败");
					MasterMain.socket=null;
					MasterMain.initGUI.bgp.setVisible(false);
				}
			}).start();
			new Thread().sleep(3000);
			if(MasterMain.socket==null){
				MasterMain.initGUI.loginPanel.setTitle(MasterMain.initGUI.loginPanel.getTitle()+"连接失败");
			}else {
				;
			}
		}catch (Exception e){

		}
	}
}
