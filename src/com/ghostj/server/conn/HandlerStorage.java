package com.ghostj.server.conn;

import com.ghostj.server.conn.client.HandleClient;
import com.ghostj.server.conn.master.HandleMaster;

import java.util.ArrayList;

public class HandlerStorage {
	public ArrayList<HandleMaster> masters=new ArrayList<>();
	public ArrayList<HandleClient> clients=new ArrayList<>();
}
