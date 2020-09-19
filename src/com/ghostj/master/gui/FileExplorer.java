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
	private static final Font FILE_NORMAL=new Font("",Font.PLAIN,20);
	public static int mode=0;
	public static final int ENTRY_MODE=0,RECT_MODE=1;
	static class FileInfo{
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
		}
		@Override
		public void paint(Graphics g){
			g.setColor(ENTRY_BG);
			g.fillRect(0,0,this.getWidth(),this.getHeight());
			if (mode==ENTRY_MODE){
				g.setColor(TEXT_CL);
				int deltay=10;
				g.drawString(info.isDir?"D":"F",0,deltay);
				g.drawString(info.name,20,deltay);
				g.drawString(((double)info.length/1000.0)+"kB", (int) (this.getWidth()*0.3),deltay);
			}else if (mode==RECT_MODE){
				//TODO 添加矩形模式
			}
		}
	}
	JPanel entryPanel=new JPanel();
	public ArrayList<FileInfo> flLs=new ArrayList<>();

	Button refresh=new Button("refresh");
	public FileExplorer(){
		this.setLayout(null);

		refresh.setBounds(0,0,60,25);
		refresh.addActionListener((e)->{
			MasterMain.writeToServer("!!rfe dir");
		});
		this.add(refresh);
		entryPanel.setLayout(null);
		entryPanel.setLocation(0,30);
		entryPanel.setSize(350,200);
		this.add(entryPanel);
	}
	public void updateEntries(){
		entryPanel.removeAll();
		entryPanel.setBackground(getBackground());
		if (mode==ENTRY_MODE) {
			int y=0;
			for (FileInfo info : flLs) {
				EntryBtn btn=new EntryBtn(info);
				btn.setSize(350,25);
				btn.setLocation(0,y);
				y+=25;
				entryPanel.add(btn);
			}
		}else {

		}
		this.repaint();
	}
}
