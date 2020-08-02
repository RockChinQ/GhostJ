package com.ghostj.server;

import com.ghostj.util.FileRW;
import com.ghostj.util.Out;

import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class HandleMaster extends Thread{
	Socket socket=null;
	BufferedReader bufferedReader=null;
	public OutputStreamWriter outputStreamWriter=null;
	public boolean available=false;//验证密码成功后才设置available=true
	public HandleMaster(Socket socket){
		this.socket=socket;
	}

	@Override
	public void run() {
		try{
			readMsg:while (true){
				String msg=null;
				try{
					msg=bufferedReader.readLine();
				}catch (Exception e){
					Out.say("HandleMaster","处理master连接时出错，正在重置连接");
					e.printStackTrace();
					ServerMain.acceptMaster.acceptable=true;
					this.stop();
				}
				//处理指令
				try{
					String cmd[]=msg.split(" ");
					//System.out.println(msg);
					switch (cmd[0]){
						case "#pw":{
							if(cmd.length<2){
								break;
							}
							if(ServerMain.masterPw.equals(cmd[1])){
								this.available=true;
								Out.say("HandleMaster","密码验证成功");
								ServerMain.tagLog.addTag("Master","login");
								ServerMain.tagLog.addTag("Master","alive");
								ServerMain.sendListToMaster();
							}else {
								outputStreamWriter.write("!passErr!");
								outputStreamWriter.flush();
								socket.close();
								outputStreamWriter.close();
								bufferedReader.close();
								Out.say("HandleMaster","密码错误");

								ServerMain.acceptMaster.acceptable=true;
								this.stop();
							}
							continue readMsg;
						}
						case "#alivem#":{
							ServerMain.checkMasterAlive.alive=true;
							ServerMain.tagLog.addTag("Master","alive");
							continue readMsg;
						}
						case "#alivems#":{
							try{
								//Out.say("HandleMaster","检测连接");
								outputStreamWriter.write("!alivems!");
								outputStreamWriter.flush();
								ServerMain.tagLog.addTag("Master","alive");
							}catch (Exception e){
								e.printStackTrace();
							}
							continue readMsg;
						}
						case "#close#":{
							try {
								outputStreamWriter.close();
								bufferedReader.close();
							}catch (Exception e){
								Out.say("HandleMaster","关闭连接失败");
								e.printStackTrace();
							}
							ServerMain.acceptMaster.acceptable=true;
							this.stop();
							continue readMsg;
						}
						case "#taglog#":{
							try {
								outputStreamWriter.write("!taglog " + FileRW.read("tagLog.json") + "!");
								outputStreamWriter.flush();
							}catch (Exception e){
								Out.say("HandleMaster","发送tagLog到master失败");
								e.printStackTrace();
							}
							continue readMsg;
						}
					}
					//已处理完master级别指令
					//不是指令的发送至下一级
					ServerMain.transCmd.handleCommand(msg);
				}catch (Exception e){
					Out.say("HandleMaster","处理master指令时出现错误");
					e.printStackTrace();
				}
			}
		}catch (Exception e){
			Out.say("HandleMaster","处理master的数据失败  非致命错误");
			e.printStackTrace();
		}
	}
}
