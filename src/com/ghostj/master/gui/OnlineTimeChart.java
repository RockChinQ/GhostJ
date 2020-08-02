package com.ghostj.master.gui;

import com.ghostj.master.MasterMain;
import com.ghostj.master.util.TagLog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class OnlineTimeChart extends JPanel {
	long startTime=0;
	int zoom=0;
	static final int DEVICE_TRACK_HEIGHT=12,LINE_HEIGHT=2;
	static final Font device=new Font("Consolas",Font.PLAIN,13);
	public OnlineTimeChart(){
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try{
					MasterMain.bufferedWriter.write("#taglog#");
					MasterMain.bufferedWriter.newLine();
					MasterMain.bufferedWriter.flush();
				}catch (Exception er){
					er.printStackTrace();
				}
			}
		});
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				try{
					MasterMain.bufferedWriter.write("#taglog#");
					MasterMain.bufferedWriter.newLine();
					MasterMain.bufferedWriter.flush();
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		},new Date(),5000);
	}
	@Override
	public void paint(Graphics g){
		setZoom(zoom);
		g.setColor(this.getBackground());
		g.fillRect(0,0,this.getWidth(),this.getHeight());

		g.setColor(Color.orange);
		Date d=new Date(startTime-10*zoom);
		g.drawString("|"+d.getDate()+","+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds(),0,10);
		d=new Date(startTime-10*zoom+this.getWidth()*zoom/4);
		g.drawString("|"+d.getDate()+","+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds(),this.getWidth()/4,10);
		d=new Date(startTime-10*zoom+this.getWidth()*zoom/4*2);
		g.drawString("|"+d.getDate()+","+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds(),this.getWidth()/4*2,10);
		d=new Date(startTime-10*zoom+this.getWidth()*zoom/4*3);
		g.drawString("|"+d.getDate()+","+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds(),this.getWidth()/4*3,10);
		d=new Date(startTime-10*zoom+this.getWidth()*zoom);
		g.drawString(d.getDate()+","+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds(),this.getWidth()/4*3+80,10);
		g.drawString("|",this.getWidth()-2,10);


		g.setColor(Color.white);
		g.setFont(device);
		int y=15;
		for(String deviceKey: MasterMain.tagLog.allOwner.keySet()){
			TagLog.tagOwner owner=MasterMain.tagLog.allOwner.get(deviceKey);
			long loginTime=0;
			for(TagLog.tagOwner.tag tag:owner.tags){
				if(tag.name.equals("login"))
					loginTime=tag.time;
				else if(tag.name.equals("alive")){
					g.setColor(Color.white);
					//System.out.println(deviceKey+" l"+loginTime+" a"+tag.time);
					g.fillRect(getXByTime(loginTime)+10,y,getXByTime(tag.time)-getXByTime(loginTime),LINE_HEIGHT);
				}
			}
			g.setColor(this.getBackground());
			g.fillRect(0,y,75,DEVICE_TRACK_HEIGHT);
			g.setColor(Color.RED);
			g.drawString(deviceKey,0,y+8);
			y+=DEVICE_TRACK_HEIGHT;
		}
		g.setColor(Color.ORANGE);
		g.drawLine(75,0,75,this.getHeight());
	}
	public int getXByTime(long time){
		return (int)(time-startTime)/zoom;
	}
	public void setZoom(int zoom){
		this.zoom=zoom;
		startTime=new Date().getTime()-(this.getWidth()*zoom);
	}
}
