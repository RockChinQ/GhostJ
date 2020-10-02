package com.ghostj.master.gui;

import com.ghostj.master.MasterMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MasterList extends JPanel {
	static final Font addrFont=new Font("Serif",Font.PLAIN,15);
	static final Font timeFont=new Font("Serif",Font.PLAIN,12);

	public static class MasterEntry extends JPanel{
		String addr,time;
		public int type=0;//0:android,1:desktop
		public MasterEntry(String addr,String time){
			this.addr=addr;
			this.time=time;
		}
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			g.setColor(Color.gray);
			g.fillRect(0,0,this.getWidth(),this.getHeight());
			g.setColor(Color.white);
			g.setFont(addrFont);
			g.drawString(addr,2,15);
			g.setFont(timeFont);
			g.drawString(time,2,30);
			g.setColor((type==0?Color.blue:Color.green));
			g.drawString((type==0?"Android":"Desktop"),95,30);
		}
	}
	public ArrayList<MasterEntry> mstEntry=new ArrayList<>();

	public MasterList(){
		this.setLayout(null);
		this.setBackground(Color.darkGray);
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MasterMain.writeToServer("#lsmst#");
			}
		});
	}
	public void updateEntries(){
		this.removeAll();
		for (int i=0;i<mstEntry.size();i++){
			mstEntry.get(i).setSize(this.getWidth(),35);
			mstEntry.get(i).setLocation(0,i*36);
			this.add(mstEntry.get(i));
		}
		this.repaint();
	}
}
