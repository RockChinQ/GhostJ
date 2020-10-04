package com.ghostj.master.conn;

import com.ghostj.client.core.ClientMain;
import com.ghostj.master.MasterMain;
import com.ghostj.master.core.BatProcess;
import com.ghostj.master.gui.ClientTable;
import com.ghostj.master.gui.FileExplorer;
import com.ghostj.master.gui.LoginPanel;
import com.ghostj.master.gui.MasterList;
import com.ghostj.master.util.FileRW;
import com.ghostj.master.util.Out;

import java.util.Map;

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
									FileRW.write("tagLog.txt",cmds.substring(8,cmds.length()-1));
//									System.out.println(cmd[1]);
									MasterMain.tagLog.load();
									MasterMain.initGUI.onlineTimeChart.resize();
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
							case "!msts":{
								System.out.println("收到master列表:"+cmds);
								MasterMain.initGUI.masterList.mstEntry.clear();
								for(int i=1;i<cmd.length;i++){
									String[] spt=cmd[i].split("\\|");
									if(spt.length<2)
										continue;
									MasterList.MasterEntry entry=new MasterList.MasterEntry(spt[0],spt[1]);
									entry.type=Integer.parseInt(spt[2]);
									MasterMain.initGUI.masterList.mstEntry.add(entry);
								}
								MasterMain.initGUI.masterList.updateEntries();
								continue;
							}
							case "!reDir":{
								String ctn[]=cmds.substring(7,cmds.length()-1).split("\\|");
								MasterMain.initGUI.fe.crtPath=(ctn.length-1)+":"+ctn[0];
								MasterMain.initGUI.fe.flLs.clear();
								System.out.println(cmds);
								if(ctn.length>1){
									for(int i=1;i<ctn.length;i++){
										String spt[]=ctn[i].split(":");
										FileExplorer.FileInfo info=new FileExplorer.FileInfo(spt[0], spt.length > 1 && Boolean.parseBoolean(spt[1]),spt.length>2?Long.parseLong(spt[2]):-1);
										MasterMain.initGUI.fe.flLs.add(info);
									}
								}
								MasterMain.initGUI.fe.updateEntries();
								continue;
							}
							case "!cd":{
								MasterMain.writeToServer("!!rfe dir");
								continue;
							}
							case "!dsk":{
								if(cmd.length>1){
									MasterMain.initGUI.fe.disks=cmd[1].toCharArray();
								}
								MasterMain.initGUI.fe.updateDisk();
								continue;
							}
							default:{
								Out.sayThisLine(cmds.toString());
								continue;
							}
						}
					}
					if (!((char)c=='!'))
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
