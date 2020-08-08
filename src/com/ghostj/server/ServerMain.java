package com.ghostj.server;

import com.ghostj.util.Config;
import com.ghostj.util.Out;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ServerMain {
	public static ArrayList<HandleConn> socketArrayList=new ArrayList<>();
	public static Config config=new Config("ghostjs.ini");
	public static int port=1033,masterPort=1034;
	public static HandleConn focusedConn=null;
	public static TransCmd transCmd=null;
	public static AcceptConn acceptConn=null;
	public static AcceptMaster acceptMaster=null;

	public static boolean manuallyTestConn=false;

	public static HandleMaster handleMaster=null;

	public static String masterPw="master123456";

	public static CheckMasterAlive checkMasterAlive=new CheckMasterAlive();
	//tag
	public static TagLog tagLog=new TagLog();
	public static void main(String[] args){
		if(new File("tagLog.json").exists()){
			tagLog.load();
		}
		tagLog.addTag(".Server","login");
		tagLog.pack();
		//加载配置文件
		port=config.getIntValue("port");
		//加载服务端
		acceptConn=new AcceptConn();
		acceptConn.start();
		Out.say("ServerMain","监听器已启动 port:"+port);

		//启动master的监听器
		acceptMaster=new AcceptMaster();
		acceptMaster.start();
		Out.say("ServerMain","master的监听器已启动 port:"+masterPort);

		transCmd=new TransCmd();
		transCmd.start();
		Out.say("ServerMain","键盘>客户端数据传输接口已启动");

		//启动检查计时器
		Timer t=new Timer();
		t.schedule(new CheckAliveTimer(),1000,15000);

		try{
			masterPw=new String(config.getStringValue("master.pw"));
			masterPort=config.getIntValue("master.port");
		}catch (Exception e){
			Out.say("ServerMain","读取master配置失败");
			config.set("master.pw","master123456");
			config.set("master.port",1034);
			config.write();
			e.printStackTrace();
		}
		//启动master的检测计时器
		new Timer().schedule(checkMasterAlive,6000,15000);
		Out.say("ServerMain","master的pw是"+masterPw);
		//启动taglog计时器
//		new Timer().schedule(new TimerTask() {
//			@Override
//			public void run() {
//				try{
//					logAliveDevice();
//				}catch (Exception e){
//					e.printStackTrace();
//				}
//			}
//		},new Date(),5000);
		Out.putPrompt();
	}
	public static void killConn(HandleConn handleConn){
		Out.say("ServerMain","kill:"+handleConn.hostName);
		if(handleConn.avai)
			tagLog.addTag(handleConn.hostName,"alive");
		if(handleConn==null)
			return;
		if(handleConn.equals(focusedConn))
			focusedConn=null;
		socketArrayList.remove(handleConn);
		try {
			sendListToMaster();
		}catch (Exception e){
			Out.say("ServerMain","无法将列表发至master");
			e.printStackTrace();
		}
		Out.say("ServerMain",handleConn.hostName+" 工作连接关闭，主机已断连");
		handleConn.stop();//管你那么多，过时的不安全的我也用
		Runtime.getRuntime().gc();

	}
	public static void sendListToMaster() throws IOException {
		//Out.say("ServerMain","尝试将列表发至master");
		if(acceptMaster.acceptable|| !handleMaster.available){
			return;
		}
		StringBuffer msg=new StringBuffer("!clients");
		for(HandleConn handleConn:socketArrayList){
			msg.append(" "+handleConn.rtIndex+" "+handleConn.hostName+" "+handleConn.connTime+" "+(handleConn==focusedConn)+" "+handleConn.version+" "+handleConn.sysStartTime);
		}
		handleMaster.outputStreamWriter.write(msg.toString()+"!");
		handleMaster.outputStreamWriter.flush();
	}
	//    public static void deleteConn(HandleConn conn){
//        socketArrayList.remove(conn);
//        conn.stop();
//    }
	public static void stopServer(int status){
		Out.say("ServerMain.stopServer","");
		logAliveDevice();
		try {
			ServerMain.handleMaster.outputStreamWriter.write("!relogin!");
			ServerMain.handleMaster.outputStreamWriter.flush();
			handleMaster.outputStreamWriter.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		System.exit(status);
	}

	public static void logAliveDevice(){
		for(HandleConn handleConn:socketArrayList){
			if(handleConn.avai)
				tagLog.addTag(handleConn.hostName,"alive");
		}
		tagLog.addTag(".Server","alive");
		if(handleMaster!=null&&handleMaster.available)
			tagLog.addTag(".Master","alive");
		tagLog.pack();
	}
	public static void cmdProcessFinish(){
		try{
			handleMaster.outputStreamWriter.write("!finish!");
			handleMaster.outputStreamWriter.flush();
		}catch (Exception e){
			//e.printStackTrace();
		}
		Out.putPrompt();
	}
}
