package com.ghostj.master.gui;

import com.ghostj.master.MasterMain;
import com.ghostj.util.Out;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;

public class ClientTable extends JPanel {

	static Font btnf=new Font("Consolas",Font.PLAIN,20);
	static Font dscf=new Font("Consolas",Font.ITALIC,14);
	public static class clientInfo{
		public long id=-1;
		public String name=null;
		public long connTime=-1;
		public boolean status=false;
		public String version=null;
		public String connTimeStr=null;
	}
	public ArrayList<clientInfo> clients=new ArrayList<>();

	public static class entry extends JButton{
		public static Color nbg=new Color(142, 141, 141),sbg=new Color(60,60,60);
		clientInfo clientInfo=null;
		public entry(clientInfo clientInfo){
			this.clientInfo=clientInfo;
			this.setSelected(false);
			this.setFont(btnf);

			Date d = new Date(clientInfo.connTime);
			clientInfo.connTimeStr=new String(d.getDate()+","+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds());
			this.addActionListener(e->{
				try{
					Out.say("ClientTable","focus");
					MasterMain.bufferedWriter.write("!focus "+clientInfo.name);
					MasterMain.bufferedWriter.newLine();
					MasterMain.bufferedWriter.flush();
				}catch (Exception err){
					err.printStackTrace();
				}
			});
		}
		@Override
		public void paint(Graphics g){
			//System.out.println(clientInfo.status+" "+(clientInfo.status?sbg:nbg).getRed());
			g.setColor(clientInfo.status?sbg:nbg);
			g.fillRect(0,0,this.getWidth(),this.getHeight());
			g.setFont(this.getFont());
			g.setColor(Color.white);
			g.drawString(clientInfo.id+" "+clientInfo.name,3,15);
			g.setFont(dscf);
			g.drawString(clientInfo.connTimeStr+" "+clientInfo.version,3,36);
		}
	}

	public static final int entryHeight=40;
	public ClientTable(){
		this.setLayout(null);
	}

	public void updateCom(){
		this.removeAll();

		//客户端列表的获取和添加在上一步进行，这里仅将列表扫描并添加entry
		int index=0;
		for(clientInfo clientInfo:clients){
			entry e=new entry(clientInfo);
			e.setSize(this.getWidth(),entryHeight);
			e.setLocation(0,(entryHeight+3)*index++);
			this.add(e);
		}
		this.repaint();
	}
}
