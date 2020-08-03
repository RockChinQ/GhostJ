package com.ghostj.master.gui;

import com.ghostj.master.MasterMain;

import javax.swing.*;
import java.awt.*;
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
	public int rangeIndex=1;

	public JTextArea console=new JTextArea();
	JScrollPane scrollPane=new JScrollPane();
	public JScrollBar scrollBar=null;
	public JTextField type=new JTextField();
	public AddStringToConsole addStringToConsole=null;

	public BatPanel batPanel;

	public LoginPanel loginPanel;

	public ArrayList<String> commandHistory=new ArrayList<>();
	public int updownPosition=0;
	public InitGUI(){
		mainwd.setSize(830,920);
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

		testConn.setSize(80,35);
		testConn.setLocation(10,6);
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
			if (clientTable.clients.size()<6)
				return;
			clientTable.tableStart=clientTable.tableStart+5>=clientTable.clients.size()?clientTable.clients.size()-5:clientTable.tableStart+1;
			clientTable.updateCom();
		});


		clientTable.setSize(185,300);
		clientTable.setLocation(10,45);
		clientTable.setBackground(null);

		onlineTimeChart.setBounds(220,45,580,175);
		onlineTimeChart.setBackground(Color.darkGray);
		onlineTimeChart.setZoom(1000);
		bgp.add(onlineTimeChart);

		displayRange.setBounds(onlineTimeChart.getX(),up.getY(),150,up.getHeight());
		displayRange.addActionListener((e)->{
			rangeIndex++;
			onlineTimeChart.setZoom((OnlineTimeChart.DISPLAY_RANGE[rangeIndex%13]/(long)onlineTimeChart.getWidth()));
			onlineTimeChart.setSep((int)(OnlineTimeChart.DISPLAY_GRID_TIME[rangeIndex%13]/onlineTimeChart.zoom));
			onlineTimeChart.repaint();
			displayRange.setText(OnlineTimeChart.DISPLAY_RANGE_DESCRI[rangeIndex%13]+" ("+OnlineTimeChart.DISPLAY_GRID_DESCRI[rangeIndex%13]+"/格)");
		});
		onlineTimeChart.setSep((int)(OnlineTimeChart.DISPLAY_GRID_TIME[rangeIndex%13]/onlineTimeChart.zoom));
		bgp.add(displayRange);
		//控制台
		console.setBounds(220,225,580,570);
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

		type.setBounds(console.getX(),console.getY()+console.getHeight()+10,console.getWidth(),40);
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

						MasterMain.bufferedWriter.write(type.getText());
						MasterMain.bufferedWriter.newLine();
						MasterMain.bufferedWriter.flush();
						commandHistory.add(type.getText());
						type.setText("");
						updownPosition=commandHistory.size();
					}catch (Exception err){
						;
					}
				}
			}
			@Override
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==KeyEvent.VK_UP){
					//System.out.println(updownPosition);
					updownPosition=updownPosition-1<0?0:updownPosition-1;
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

				}
			}
		});
		type.requestFocus();

		bgp.add(type);

		//批处理指令列表
		batPanel=new BatPanel();
		batPanel.setBackground(bgp.getBackground());
		batPanel.setBounds(clientTable.getX(),clientTable.getY()+clientTable.getHeight()+10,200,bgp.getHeight());
		batPanel.setSize(170,bgp.getHeight());
		bgp.add(batPanel);

		bgp.add(clientTable);

		bgp.setVisible(false);

		//mainwd.setSize(console.getX()+console.getWidth(),mainwd.getHeight());
		mainwd.setVisible(true);

		loginPanel=new LoginPanel(mainwd);
	}
}
