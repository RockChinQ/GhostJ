package com.ghostj.server.conn;

import com.ghostj.server.conn.client.HandleClient;
import com.ghostj.server.conn.master.HandleMaster;
import com.ghostj.server.core.ServerMain;
import com.ghostj.server.log.Log;

import java.util.ArrayList;

/**
 * 储存master和client连接管理器，并提供相关管理方法
 */
public class HandlerStorage {
	public ArrayList<HandleMaster> masters=new ArrayList<>();
	public ArrayList<HandleClient> clients=new ArrayList<>();

	/**
	 * 向拥有特定标签和特定值的master发送数据
	 * @param key 标签键
	 * @param value 值
	 * @param msg 消息
	 */
	public void writeMasterIndexedByTag(String key,String value,String msg){
		masters.forEach((master)->{
			if(master.equals(key,value)){
				try {
					master.writeUTF(msg);
				} catch (Exception e) {
					ServerMain.log.puts(Log.NOTIFICATION|Log.ERROR,"%CLASS%","无法发送信息到master:"+master.getTagValue("name"));
				}
			}
		});
	}
}
