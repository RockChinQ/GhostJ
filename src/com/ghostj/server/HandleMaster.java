package com.ghostj.server;

import com.ghostj.util.Out;

import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class HandleMaster extends Thread{
	Socket socket=null;
	BufferedReader bufferedReader=null;
	OutputStreamWriter outputStreamWriter=null;
	boolean available=false;//验证密码成功后才设置available=true
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
					System.out.println(msg);
					switch (cmd[0]){
						case "#pw":{
							if(cmd.length<2){
								break;
							}
							if(ServerMain.masterPw.equals(cmd[1])){
								this.available=true;
								Out.say("HandleMaster","密码验证成功");

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
