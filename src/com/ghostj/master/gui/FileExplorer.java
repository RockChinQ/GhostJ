package com.ghostj.master.gui;

import com.ghostj.master.MasterMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileExplorer extends JPanel {
	private static final Color ENTRY_BG=new Color(75, 75, 75),ENTRY_BG_SELECT=new Color(55,105,150);
	private static final Color TEXT_CL=new Color(200,200,200);
	private static final Color FILE_ICON_CL =new Color(234, 246, 255);
	private static final Font FILE_NORMAL=new Font("Serif",Font.PLAIN,17);
	public static int mode=0;
	public static final int ENTRY_MODE=0,RECT_MODE=1;
	public static Date d=new Date();
	public static String dateStr=(d.getYear()+1900)+"-"+(d.getMonth()+1)+"-"+d.getDate();
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
	class EntryBtn extends JPanel{
		FileInfo info;
		FileIcon icon;
		boolean select=false;

		public boolean isSelect() {
			return select;
		}

		public void setSelect(boolean select) {
			this.select = select;
		}

		EntryBtn(FileInfo info){
			this.info=info;
			icon=new FileIcon(info);
			this.setLayout(null);
			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					if (e.getClickCount()==2){
						if (info.isDir){
							MasterMain.writeToServer("!!rfe cd "+info.name.replaceAll(" ","?"));
						}else {
							MasterMain.writeToServer("!!rfe upload "+info.getName().replaceAll(" ","?")+" files/"+dateStr);
						}
					}else if(e.getClickCount()==1){
						if (!e.isControlDown()){
							if (e.isShiftDown()){
								setSelect(!isSelect());
								selectRange();
							}else {
								setAllSelect(false);
								setSelect(true);
							}
						}else {
							setSelect(!isSelect());
						}
						repaintAll();
					}
				}
			});
		}
		@Override
		public void paint(Graphics g){
			g.setColor(isSelect()?ENTRY_BG_SELECT:ENTRY_BG);
			g.setFont(getFont());
			g.fillRect(0,0,this.getWidth(),this.getHeight());
			if (mode==ENTRY_MODE){
				FontMetrics fm=g.getFontMetrics(getFont());
				g.drawImage(icon,0,0,this);
				g.setColor(TEXT_CL);
				int deltay=15;
				//g.drawString(info.isDir?"D":"F",0,deltay);
				String lenStr=formatTosepara((float) ((float) info.length/1000.0))+"kB";
				g.drawString(lenStr,getWidth()-fm.stringWidth(lenStr)-4,deltay);
				g.drawString(info.name, 30,deltay);
			}else if (mode==RECT_MODE){
				//TODO 添加矩形模式
			}
		}
		public final String formatTosepara(float data) {
			DecimalFormat df = new DecimalFormat("#,###.000");
			return df.format(data);
		}
	}
	private static class HomePath extends JButton{
		public HomePath(){
			this.addActionListener(e -> {
				MasterMain.writeToServer("!!rfe cd %GHOSTJ_HOME%");
			});
		}
		@Override
		public void paint(Graphics g) {
			g.setColor(Color.gray);
			g.fillRect(0,0,getWidth(),getHeight());
			g.setColor(Color.cyan);
			g.drawLine(15,3,5,8);
			g.drawLine(15,3,25,8);
			g.drawRect(9,8,12,12);
		}
	}
	private static class FileIcon extends BufferedImage{
		FileInfo info;
		public FileIcon(FileInfo info){
			super(20,20,BufferedImage.TYPE_INT_ARGB);
			this.info=info;
			this.createGraphics();
			paint();
		}
		private void paint(){
			Graphics g=getGraphics();
			g.setColor(ENTRY_BG);
			g.fillRect(0,0,getWidth(),getHeight());
			g.setColor(info.isDir?Color.orange: FILE_ICON_CL);
			g.fillRect(3,3,getWidth()-6,getHeight()-6);
		}
	}
	JPanel entryPanel=new JPanel();
	JPanel diskPanel=new JPanel();
	HomePath homePath=new HomePath();
	JScrollPane scrollPane=new JScrollPane(entryPanel);
	public ArrayList<FileInfo> flLs=new ArrayList<>();
	public ArrayList<EntryBtn> btn=new ArrayList<>();
	public char[] disks;

	Button refresh=new Button("dir");
	Button upper=new Button("<-");
	Button selectRange=new Button("<->");

	Button uploadAll=new Button("^up");
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
		homePath.setBounds(upper.getX()+upper.getWidth()+5,refresh.getY(),30,25);
		this.add(homePath);

		selectRange.setBounds(homePath.getX()+homePath.getWidth()+7,homePath.getY(),30,25);
		selectRange.addActionListener(e -> {
			selectRange();
			repaintAll();
		});
		this.add(selectRange);

		crtLb.setBounds(selectRange.getX()+selectRange.getWidth()+5,refresh.getY(),600,30);
		crtLb.setForeground(TEXT_CL);
		this.add(crtLb);

		uploadAll.setBounds(0,refresh.getY()+refresh.getHeight()+3,40,25);
		uploadAll.addActionListener(e -> {
			for(EntryBtn btn:this.btn){
				if(btn.isSelect()&&!btn.info.isDir()){
					MasterMain.writeToServer("!!rfe upload "+btn.info.getName().replaceAll(" ","?")+" files/"+dateStr);
				}
			}
		});
		this.add(uploadAll);

		diskPanel.setLayout(null);
		diskPanel.setBounds(0,uploadAll.getY()+uploadAll.getHeight()+4,500,30);
		diskPanel.setBackground(Color.darkGray);
		this.add(diskPanel);

		entryPanel.setLayout(null);
		entryPanel.setLocation(0,diskPanel.getY()+diskPanel.getHeight()+5);
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
		MasterMain.initGUI.type.requestFocus();
	}
	public void setAllSelect(boolean select){
		for(EntryBtn btn:this.btn){
			btn.setSelect(select);
		}
		entryPanel.repaint();
	}
	public void selectRange(){
		int first=-1,second=0;
		for(int i=0;i<btn.size();i++){
			if(btn.get(i).isSelect()) {
				if (first == -1) {
					first = i;
				}
				second = i;
			}
		}
		for(int i=first;i<second;i++){
			btn.get(i).setSelect(true);
		}
	}
	public void updateEntries(){
		btn.clear();
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
				this.btn.add(btn);
				y+=25;
				entryPanel.add(btn);
			}
			this.entryPanel.setPreferredSize(new Dimension(entryPanel.getWidth()-20,y+25));
			this.scrollPane.validate();
		}else {

		}
		this.repaint();
		MasterMain.initGUI.type.requestFocus();
	}
	public void repaintAll(){
		this.repaint();
	}
}
