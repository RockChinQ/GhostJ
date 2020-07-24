package com.ghostj.master.conn;

import com.ghostj.master.MasterMain;
import com.ghostj.master.gui.ClientTable;
import com.ghostj.master.gui.LoginPanel;
import com.ghostj.master.util.Out;

public class HandleConn extends Thread{

	@Override
	public void run(){
		int c=0;
		try {
			while (true) {
				try{
					c=MasterMain.inputStreamReader.read();

					if((char)c=='!'){
						StringBuffer cmds=new StringBuffer("");
						int workInfoLen=0;
						while(true){
							int c0=MasterMain.inputStreamReader.read();
							if((char)c0=='!') {
								//cmds.append("");
								break;
							}
							if ((char)c0=='\n') {
								cmds.append("\n");
								break;
							}
							cmds.append((char)c0);
							workInfoLen++;
//							if(workInfoLen>=25)
//								break;
						}
						System.out.println(cmds);
						String cmd[]=cmds.toString().split(" ");
						switch (cmd[0]){
							case "close":{
								kill("连接被关闭");
								continue;
							}
							case "passErr":{
								kill("密码错误");
								continue;
							}
							case "clients":{//获取到最新列表
								Out.say("HandleConn","接收到客户端列表");
								MasterMain.initGUI.clientTable.clients.clear();
								for(int i=1;i<cmd.length;i+=5){
									ClientTable.clientInfo clientInfo=new ClientTable.clientInfo();
									clientInfo.id=Long.parseLong(cmd[i]);
									clientInfo.name=cmd[i+1];
									clientInfo.connTime=Long.parseLong(cmd[i+2]);
									clientInfo.status=Boolean.parseBoolean(cmd[i+3]);
									clientInfo.version= cmd[i + 4];
									MasterMain.initGUI.clientTable.clients.add(clientInfo);
								}
								MasterMain.initGUI.clientTable.updateCom();
								continue;
							}
							default:{
								Out.sayThisLine(cmds.toString());
							}
						}
					}
					Out.sayThisLine((char)c);
				}catch (Exception e){
					Out.say("HandleConn","接受数据出错，连接正在重置");
					e.printStackTrace();
					kill("连接已断开");
					MasterMain.initGUI.bgp.setVisible(false);
				}
			}
		}catch (Exception e){
			Out.say("HandleConn","处理连接数据时出错 非致命错误");
			e.printStackTrace();
		}
	}
	public void kill(String msg){

		MasterMain.socket=null;
		MasterMain.inputStreamReader=null;
		MasterMain.initGUI.loginPanel=new LoginPanel(MasterMain.initGUI.mainwd);
		MasterMain.initGUI.loginPanel.setTitle("Login-"+msg);
		this.stop();
	}
}
