package com.ghostj.master.conn;

import com.ghostj.master.MasterMain;
import com.ghostj.master.core.BatProcess;
import com.ghostj.master.gui.ClientTable;
import com.ghostj.master.gui.LoginPanel;
import com.ghostj.master.util.FileRW;
import com.ghostj.master.util.Out;

public class HandleConn extends Thread{

	@Override
	public void run(){
		int c=0;
		try {
			while ((c=MasterMain.inputStreamReader.read())!=-1) {
				try{
					;

					if((char)c=='!'){
						StringBuffer cmds=new StringBuffer("!");
						while(true){
							int c0=MasterMain.inputStreamReader.read();
							if((char)c0=='!') {
								cmds.append("!");
								break;
							}
							if ((char)c0=='\n') {
								cmds.append("\n");
								break;
							}
							cmds.append((char)c0);
							//							if(workInfoLen>=25)
//								break;
						}
//						System.out.println(cmds);
						String cmd[]=cmds.toString().substring(0,cmds.length()-1).split(" ");
						switch (cmd[0]){
							case "!relogin":{
								kill("被server断开连接");
								continue;
							}
							case "!alivem":{
								MasterMain.bufferedWriter.write("#alivem#");
								MasterMain.bufferedWriter.newLine();
								MasterMain.bufferedWriter.flush();
								continue;
							}
							case "!alivems":{
								MasterMain.checkServerAlive.alive=true;
								continue;
							}
							case "!passErr":{
								kill("密码错误");
								continue;
							}
							case "!clients":{//获取到最新列表
								Out.say("HandleConn","接收到客户端列表");
								MasterMain.initGUI.clientTable.clients.clear();
								for(int i=1;i<cmd.length;i+=6){
									ClientTable.clientInfo clientInfo=new ClientTable.clientInfo();
									clientInfo.id=Long.parseLong(cmd[i]);
									clientInfo.name=cmd[i+1];
									clientInfo.connTime=Long.parseLong(cmd[i+2]);
									clientInfo.status=Boolean.parseBoolean(cmd[i+3]);
									clientInfo.version= cmd[i + 4];
									clientInfo.sysStartTime=Long.parseLong(cmd[i+5]);
									MasterMain.initGUI.clientTable.clients.add(clientInfo);
								}
								MasterMain.initGUI.clientTable.tableStart=MasterMain.initGUI.clientTable.tableStart+5>MasterMain.initGUI.clientTable.clients.size()?0:MasterMain.initGUI.clientTable.tableStart;
								MasterMain.initGUI.clientTable.updateCom();
								continue;
							}
							case "!taglog":{
								try {
									FileRW.write("tagLog.txt",cmds.substring(8,cmds.length()));
//									System.out.println(cmd[1]);
									MasterMain.tagLog.load();
									MasterMain.initGUI.onlineTimeChart.repaint();
								}catch (Exception e){
									Out.say("HandleConn","获取tagLog失败");
									e.printStackTrace();
								}
								continue;
							}
							case "!finish":{
//								Out.say("HandleConn","未预期的finish消息");
//								MasterMain.initGUI.addStringToConsole.addStr("\n[HandleConn]收到未预期的finish消息\n");

								BatProcess.masterProcess();

								continue;
							}
							default:{
								Out.sayThisLine(cmds.toString());
								continue;
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
			kill("server被关闭");
		}catch (Exception e){
			Out.say("HandleConn","处理连接数据时出错 非致命错误");
			e.printStackTrace();
		}
	}
	public void kill(String msg){

		try{
			MasterMain.bufferedWriter.write("#close#");
			MasterMain.bufferedWriter.newLine();
			MasterMain.bufferedWriter.flush();
		}catch (Exception e){
			e.printStackTrace();
		}
		MasterMain.socket=null;
		MasterMain.inputStreamReader=null;
		MasterMain.initGUI.loginPanel=new LoginPanel(MasterMain.initGUI.mainwd);
		MasterMain.initGUI.loginPanel.setTitle("Login-"+msg);
		this.stop();
	}
}
