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
	long zoom=0;
	static final int DEVICE_TRACK_HEIGHT=12,LINE_HEIGHT=2;
	static final Font device=new Font("Consolas",Font.PLAIN,13);
	public static final long[] DISPLAY_RANGE=new long[]{300000,600000,1800000,3600000,21600000,43200000,86400000,604800000
			,1209600000, 2592000000L, 5184000000L, 15552000000L, 31536000000L};
	public static final String[] DISPLAY_RANGE_DESCRI=new String[]{"5分钟","10分钟","30分钟","1小时","6小时","12小时","1天","7天","14天","30天","60天","180天","1年"};
	//5分钟，10分钟，30分钟，1小时，6小时，12小时，1天，7天，14天，30天，60天，180天，365天
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
		Date d=new Date(startTime);
		g.drawString("|"+(d.getMonth()+1)+"."+d.getDate()+","+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds(),0,10);
		d=new Date(startTime+this.getWidth()*zoom/4);
		g.drawString("|"+(d.getMonth()+1)+"."+d.getDate()+","+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds(),this.getWidth()/4,10);
		d=new Date(startTime+this.getWidth()*zoom/4*2);
		g.drawString("|"+(d.getMonth()+1)+"."+d.getDate()+","+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds(),this.getWidth()/4*2,10);
		d=new Date(startTime+this.getWidth()*zoom/4*3);
		g.drawString("|"+(d.getMonth()+1)+"."+d.getDate()+","+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds(),this.getWidth()/4*3,10);
		d=new Date(startTime+this.getWidth()*zoom);
		g.drawString((d.getMonth()+1)+"."+d.getDate()+","+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds(),this.getWidth()/4*3+76,10);
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
					g.fillRect((int)getXByTime(loginTime),y,(int)getXByTime(tag.time)-(int)getXByTime(loginTime),LINE_HEIGHT);
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
	public long getXByTime(long time){
		return (time-startTime)/zoom;
	}
	public void setZoom(long zoom){
		this.zoom=zoom;
		startTime=new Date().getTime()-((long)this.getWidth()*zoom);
	}
}
