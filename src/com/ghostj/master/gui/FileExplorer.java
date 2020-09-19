package com.ghostj.master.gui;

import com.ghostj.master.MasterMain;
import com.ghostj.master.conn.HandleConn;

import javax.crypto.MacSpi;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FileExplorer extends JPanel {
	private static final Color ENTRY_BG=new Color(40,40,40);
	private static final Color TEXT_CL=new Color(200,200,200);
	private static final Color ICON_CL=new Color(39, 171, 248);
	private static final Font FILE_NORMAL=new Font("Serif",Font.PLAIN,15);
	public static int mode=0;
	public static final int ENTRY_MODE=0,RECT_MODE=1;
	public static class FileInfo{
		String name;
		boolean isDir;
		long length;
		public FileInfo(String name,boolean isDir,long length){
			this.name=name;
			this.length=length;
			this.isDir=isDir;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isDir() {
			return isDir;
		}

		public void setDir(boolean dir) {
			isDir = dir;
		}

		public long getLength() {
			return length;
		}

		public void setLength(long length) {
			this.length = length;
		}
	}
	static class EntryBtn extends JButton{
		FileInfo info;
		EntryBtn(FileInfo info){
			this.info=info;
			this.addActionListener((e)->{
				if (info.isDir){
					MasterMain.writeToServer("!!rfe cd "+info.name.replaceAll(" ","?"));
				}
			});
		}
		@Override
		public void paint(Graphics g){
			g.setColor(ENTRY_BG);
			g.setFont(getFont());
			g.fillRect(0,0,this.getWidth(),this.getHeight());
			if (mode==ENTRY_MODE){
				g.setColor(TEXT_CL);
				int deltay=15;
				g.drawString(info.isDir?"D":"F",0,deltay);
				g.drawString(info.name,20,deltay);
				g.drawString(((double)info.length/1000.0)+"kB", (int) (this.getWidth()*0.6),deltay);
			}else if (mode==RECT_MODE){
				//TODO 添加矩形模式
			}
		}
	}
	JPanel entryPanel=new JPanel();
	JPanel diskPanel=new JPanel();
	JScrollPane scrollPane=new JScrollPane(entryPanel);
	public ArrayList<FileInfo> flLs=new ArrayList<>();
	public char[] disks;

	Button refresh=new Button("dir");
	Button upper=new Button("<-");
	public String crtPath;
	JLabel crtLb=new JLabel("<refresh>");
	public FileExplorer(){
		this.setLayout(null);

		refresh.setBounds(0,0,40,25);
		refresh.addActionListener((e)->{
			MasterMain.writeToServer("!!rfe dir");
			MasterMain.writeToServer("!!rfe dsk");
		});
		this.add(refresh);
		upper.setBounds(refresh.getX()+refresh.getWidth()+2,0,30,25);
		upper.addActionListener((e)->{
			MasterMain.writeToServer("!!rfe cd ..\\");
		});
		this.add(upper);
		crtLb.setBounds(upper.getX()+upper.getWidth()+5,refresh.getY(),600,30);
		crtLb.setForeground(TEXT_CL);
		this.add(crtLb);

		diskPanel.setLayout(null);
		diskPanel.setBounds(0,refresh.getY()+refresh.getHeight()+4,500,30);
		diskPanel.setBackground(Color.darkGray);
		this.add(diskPanel);

		entryPanel.setLayout(null);
		entryPanel.setLocation(0,60);
		entryPanel.setSize(450,700);
		entryPanel.setPreferredSize(new Dimension(450,700));
		scrollPane.setBounds(entryPanel.getX(),entryPanel.getY(),entryPanel.getWidth(),entryPanel.getHeight());
		scrollPane.validate();
		this.add(scrollPane);
	}
	public void updateDisk(){
		diskPanel.removeAll();
		int x=0;
		for (char disk:disks){
			Button d=new Button(disk+":\\");
			d.setBounds(x,0,30,30);
			d.addActionListener((e)->{
				MasterMain.writeToServer("!!rfe cd "+(disk+":\\"));
			});
			diskPanel.add(d);
			x+=31;
		}
		this.repaint();
	}
	public void updateEntries(){
		entryPanel.removeAll();
		entryPanel.setBackground(getBackground());
		crtLb.setText(crtPath);
		entryPanel.setSize(this.getWidth(),this.getHeight()-20);
		if (mode==ENTRY_MODE) {
			int y=0;
			for (FileInfo info : flLs) {
				EntryBtn btn=new EntryBtn(info);
				btn.setSize(430,25);
				btn.setLocation(0,y);
				btn.setFont(FILE_NORMAL);
				y+=25;
				entryPanel.add(btn);
			}
			this.entryPanel.setPreferredSize(new Dimension(entryPanel.getWidth()-20,y+25));
			this.scrollPane.validate();
		}else {

		}
		this.repaint();
	}
}
