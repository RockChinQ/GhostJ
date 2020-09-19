package com.ghostj.master.gui;

import com.ghostj.master.MasterMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class InitGUI {
	static final Font cf=new Font("微软雅黑",Font.PLAIN,15);
	public JFrame mainwd=new JFrame();
	public ClientTable clientTable=new ClientTable();
	public JPanel bgp=new JPanel();

	public Button testConn=new Button("Test");
	Button up=new Button("-"),down=new Button("+");

	public OnlineTimeChart onlineTimeChart=new OnlineTimeChart();
	public Button displayRange=new Button("10分钟 (1分钟/格)");
	public Button left=new Button("<"),right=new Button(">"),reset=new Button("↔");
	public int rangeIndex=1;

	public JTextArea console=new JTextArea();
	JScrollPane scrollPane=new JScrollPane();
	public JScrollBar scrollBar=null;
	public JTextField type=new JTextField();
	//echo模式或指令模式
	public Button typeMode=new Button("Echo×");
	public AddStringToConsole addStringToConsole=null;

	public BatPanel batPanel;

	public LoginPanel loginPanel;

	public ArrayList<String> commandHistory=new ArrayList<>();
	public int updownPosition=0;

	public FileExplorer fe=new FileExplorer();

	public InitGUI(){
		mainwd.setSize(900,920);
		mainwd.setLocation(200,100);
		mainwd.setLayout(null);
		mainwd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainwd.setBackground(new Color(80,80,80));
		mainwd.setTitle("Master Of GhostJ.Powered by RockChin");

		bgp.setSize(mainwd.getSize());
		bgp.setLocation(0,0);
		bgp.setLayout(null);
		bgp.setBackground(new Color(80,80,80));
		mainwd.add(bgp);

		//批处理指令列表
		batPanel=new BatPanel();
		batPanel.setBackground(bgp.getBackground());
		batPanel.setBounds(10,clientTable.getY(),200,bgp.getHeight());
		batPanel.setSize(170,bgp.getHeight());
		bgp.add(batPanel);


		clientTable.setSize(185,1000);
		clientTable.setLocation(batPanel.getWidth()+batPanel.getX()+15,45);
		clientTable.setBackground(null);
		bgp.add(clientTable);


		testConn.setSize(80,35);
		testConn.setLocation(clientTable.getX(),6);
		testConn.addActionListener(e->{
			try{
				MasterMain.bufferedWriter.write("!test 200");
				MasterMain.bufferedWriter.newLine();
				MasterMain.bufferedWriter.flush();
			}catch (Exception err){
				err.printStackTrace();
			}
		});
		bgp.add(testConn);
		//上下翻页
		up.setBounds(testConn.getX()+testConn.getWidth()+5,testConn.getY(),40,35);
		down.setBounds(testConn.getX()+testConn.getWidth()+up.getWidth()+7,testConn.getY(),40,35);
		bgp.add(up);
		bgp.add(down);

		up.addActionListener(e -> {
			clientTable.tableStart=clientTable.tableStart<=0?0:clientTable.tableStart-1;
			clientTable.updateCom();
		});
		down.addActionListener(e -> {
			if (clientTable.clients.size()<14)
				return;
			clientTable.tableStart=clientTable.tableStart+5>=clientTable.clients.size()?clientTable.clients.size()-13:clientTable.tableStart+1;
			clientTable.updateCom();
		});

		onlineTimeChart.setBounds(clientTable.getX()+clientTable.getWidth()+10,45,650,225);
		onlineTimeChart.setBackground(Color.darkGray);
		onlineTimeChart.setZoom(1000);
		bgp.add(onlineTimeChart);

		displayRange.setBounds(onlineTimeChart.getX(),up.getY(),150,up.getHeight());
		int rangeList=OnlineTimeChart.DISPLAY_RANGE.length;
		displayRange.addActionListener((e)->{
			rangeIndex++;
			onlineTimeChart.setZoom((OnlineTimeChart.DISPLAY_RANGE[rangeIndex%rangeList]/(long)onlineTimeChart.getWidth()));
			onlineTimeChart.setSep((int)(OnlineTimeChart.DISPLAY_GRID_TIME[rangeIndex%rangeList]/onlineTimeChart.zoom));
			onlineTimeChart.repaint();
			displayRange.setText(OnlineTimeChart.DISPLAY_RANGE_DESCRI[rangeIndex%rangeList]+" ("+OnlineTimeChart.DISPLAY_GRID_DESCRI[rangeIndex%rangeList]+"/格)");
		});
		onlineTimeChart.setSep((int)(OnlineTimeChart.DISPLAY_GRID_TIME[rangeIndex%rangeList]/onlineTimeChart.zoom));
		bgp.add(displayRange);

		left.setBounds(displayRange.getX()+displayRange.getWidth()+20,displayRange.getY(),up.getWidth(),up.getHeight());
		bgp.add(left);
		reset.setBounds(left.getX()+left.getWidth()+2,left.getY(),left.getWidth(),left.getHeight());
		bgp.add(reset);
		right.setBounds(reset.getX()+reset.getWidth()+2,reset.getY(),reset.getWidth(),reset.getHeight());
		bgp.add(right);


		left.addActionListener((e)->{
			onlineTimeChart.x_addition+=onlineTimeChart.sep;
			onlineTimeChart.repaint();
		});
		reset.addActionListener((e)->{
			onlineTimeChart.x_addition=0;
			onlineTimeChart.repaint();
		});
		right.addActionListener((e)->{
			onlineTimeChart.x_addition-=onlineTimeChart.sep;
			onlineTimeChart.repaint();
		});
		//控制台
		console.setBounds(onlineTimeChart.getX(),onlineTimeChart.getY()+onlineTimeChart.getHeight()+10,650,bgp.getHeight()-onlineTimeChart.getHeight()-onlineTimeChart.getY()-115);
		console.setBackground(Color.darkGray);
		console.setForeground(new Color(255, 255, 255, 255));
		console.setFont(cf);
		console.setEditable(false);
		scrollPane.setBounds(console.getBounds());
		scrollPane.setViewportView(console);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollBar=scrollPane.getVerticalScrollBar();

		addStringToConsole=new AddStringToConsole();
		bgp.add(scrollPane);

		type.setBounds(console.getX(),console.getY()+console.getHeight()+10,console.getWidth()-70,40);
		type.setBackground(console.getBackground());
		type.setForeground(console.getForeground());
		type.setFont(console.getFont());
		type.setCaretColor(Color.white);
		type.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				super.keyTyped(e);
				//System.out.println(e.getKeyChar());
				if(e.getKeyChar()==KeyEvent.VK_ENTER){
					try{

						String cmd=typeMode.isSelected()?("!echo "+type.getText()):type.getText();
						MasterMain.bufferedWriter.write(cmd);
						MasterMain.bufferedWriter.newLine();
						MasterMain.bufferedWriter.flush();
						commandHistory.add(cmd);
						type.setText("");
						updownPosition=commandHistory.size();
					}catch (Exception err){
						MasterMain.handleConn.kill("连接已被重置");
					}
				}
			}
			@Override
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==KeyEvent.VK_UP){
					//System.out.println(updownPosition);
					updownPosition= Math.max(updownPosition - 1, 0);
					type.setText(commandHistory.get(updownPosition));
				}else if(e.getKeyCode()==KeyEvent.VK_DOWN){
					if(commandHistory.size()==0)
						return;
					if(updownPosition==commandHistory.size()-1){
						type.setText("");
						updownPosition=commandHistory.size();
						return;
					}
					updownPosition=updownPosition+1>=commandHistory.size()?commandHistory.size()-1:updownPosition+1;
					type.setText(commandHistory.get(updownPosition));

				}else if((e.getExtendedKeyCode()=='e'||e.getExtendedKeyCode()=='E')&&e.isControlDown()){
					typeMode.setSelected(!typeMode.isSelected());
					typeMode.setText("Echo"+(typeMode.isSelected()?"√":"×"));
					type.requestFocus();
				}
//				System.out.println(e.getKeyChar());
			}
		});
		type.requestFocus();
		bgp.add(type);


		typeMode.setBounds(type.getX()+type.getWidth()+10,console.getY()+console.getHeight()+10,70,40);
		typeMode.addActionListener((e)->{
			typeMode.setSelected(!typeMode.isSelected());
//			System.out.println(typeMode.isSelected());
			typeMode.setText("Echo"+(typeMode.isSelected()?"√":"×"));
			type.requestFocus();
		});
		typeMode.setVisible(true);
		bgp.add(typeMode);

		fe.setBounds(console.getX()+console.getWidth()+10,onlineTimeChart.getY(),450,800);
		fe.setBackground(Color.darkGray);
		bgp.add(fe);

		bgp.setVisible(false);
		mainwd.setSize(fe.getX()+fe.getWidth()+40,mainwd.getHeight());
		bgp.setSize(mainwd.getWidth()+15,mainwd.getHeight()+10);
		//mainwd.setSize(console.getX()+console.getWidth(),mainwd.getHeight());
		mainwd.getContentPane().setBackground(bgp.getBackground());
		mainwd.setVisible(true);
/*
		mainwd.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				bgp.setSize(mainwd.getSize());
				console.setSize(mainwd.getWidth()-clientTable.getWidth()-10);
			}
		});*/

		loginPanel=new LoginPanel(mainwd);
	}
}
