package com.ghostj.server_old;

import com.ghostj.util.FileRW;
import com.ghostj.util.Out;
import com.ghostj.util.TimeUtil;

import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class HandleMaster extends Thread{
	Socket socket=null;
	BufferedReader bufferedReader=null;
	public OutputStreamWriter outputStreamWriter=null;
	public boolean available=false;//验证密码成功后才设置available=true
	public long connTime=new Date().getTime();
	public HandleMaster(Socket socket){
		this.socket=socket;
		queueThread.start();
	}

	QueueThread queueThread=new QueueThread();
	//标签集
	ArrayList<String> attributes=new ArrayList<>();

	public boolean alive=true;
	@Override
	public void run() {
		//新建master连接
		ServerMain.sendMasterList();
		try{
			readMsg:while (true){
				String msg=null;
				try{
					msg=bufferedReader.readLine();
				}catch (Exception e){
					Out.say("HandleMaster","处理master连接时出错，正在重置连接");
					e.printStackTrace();
					ServerMain.tagLog.addTag(".Master","alive");
//					ServerMain.handleMaster.available=false;
//					ServerMain.acceptMaster.acceptable=true;
//					this.stop();
					CheckMasterAlive.kill(this);
				}
				//处理指令
				try{
					String cmd[] = new String[0];
					try {
						cmd = msg.split(" ");
					}catch (Exception er){

						Out.say("HandleMaster","master断连");
						ServerMain.tagLog.addTag(".Master","alive");
						CheckMasterAlive.kill(this);
					}
					//System.out.println(msg);
					switch (cmd[0]){
						case "#pw":{
							if(cmd.length<2){
								break;
							}
							if(ServerMain.masterPw.equals(cmd[1])){
								this.available=true;
								Out.say("HandleMaster","密码验证成功");
								ServerMain.sendListToMaster();
								ServerMain.tagLog.addTag(".Master","login");
								sleep(20);
								ServerMain.tagLog.addTag(".Master","alive");
								Out.say("NOTE",ServerMain.note.toString());
							}else {
								addMsg("!passErr!");
								CheckMasterAlive.kill(this);
								Out.say("HandleMaster","密码错误");
								this.stop();
							}
							Out.putPrompt();
							continue readMsg;
						}
						case "#alivem#":{
							ServerMain.checkMasterAlive.alive=true;
							if(available)
								ServerMain.tagLog.addTag(".Master","alive");
							continue readMsg;
						}
						case "#alivems#":{
							try{
								//Out.say("HandleMaster","检测连接");
								addMsg("!alivems!");
								if(available)
									ServerMain.tagLog.addTag(".Master","alive");
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
							CheckMasterAlive.kill(this);
							this.stop();
							continue readMsg;
						}
						case "#taglog#":{
							try {
								ServerMain.logAliveDevice();
								String  write="!taglog " + FileRW.read("tagLog.txt") + "!";
//								System.out.println(write);
								addMsg(write);
							}catch (Exception e){
								Out.say("HandleMaster","发送tagLog到master失败");
								e.printStackTrace();
							}
							continue readMsg;
						}
						case "#lsmst#":{
							StringBuffer msts=new StringBuffer("!msts");
							for(HandleMaster master:AcceptMaster.masters){
								msts.append(" "+master.socket.getInetAddress()+":"+master.socket.getPort()+"|"+TimeUtil.millsToMMDDHHmmSS(master.connTime)+"|"+(master.attributes.contains("desktop")?1:0));
							}
							msts.append("!");
							try{
								addMsg(msts.toString());
							}catch (Exception e){
								e.printStackTrace();
							}
							continue readMsg;
						}
						case "#attri":{
							attributes.add(cmd[1]);
							ServerMain.sendMasterList();
							continue readMsg;
						}
					}
					//已处理完master级别指令
					//不是指令的发送至下一级
					//调用TransCmd的handlecommand
					if(available)
						ServerMain.transCmd.handleCommand(msg);
				}catch (Exception e){
					Out.say("HandleMaster","处理master指令时出现错误");
					e.printStackTrace();
				}
			}
		}catch (Exception e){
			Out.say("HandleMaster","处理master的数据失败");
			e.printStackTrace();
		}
	}
	private final ArrayList<String> msgQueue=new ArrayList<>();
	public class QueueThread extends Thread{
		@Override
		public void run(){
			while(true){
				synchronized (msgQueue) {
					if (msgQueue.size() > 0) {
						sent(msgQueue.remove(0));
					}else {
						try {
							msgQueue.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	private void sent(String msg){
		try{
			outputStreamWriter.write(msg);
			outputStreamWriter.flush();
		}catch (Exception e){
			e.printStackTrace();
			CheckMasterAlive.kill(this);
		}
	}
	public void addMsg(String msg){
		synchronized (msgQueue){
			msgQueue.add(msg);
			msgQueue.notify();
		}
	}
}
