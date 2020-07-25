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
	public JTextArea console=new JTextArea();
	JScrollPane scrollPane=new JScrollPane();
	public JScrollBar scrollBar=null;
	public JTextField type=new JTextField();
	public AddStringToConsole addStringToConsole=null;

	public LoginPanel loginPanel;

	public ArrayList<String> commandHistory=new ArrayList<>();
	public int updownPosition=0;
	public InitGUI(){
		mainwd.setSize(900,720);
		mainwd.setLocation(200,100);
		mainwd.setLayout(null);
		mainwd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainwd.setBackground(new Color(80,80,80));

		bgp.setSize(mainwd.getSize());
		bgp.setLocation(0,0);
		bgp.setLayout(null);
		bgp.setBackground(new Color(80,80,80));
		mainwd.add(bgp);

		testConn.setSize(80,30);
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

		clientTable.setSize(185,this.mainwd.getHeight());
		clientTable.setLocation(10,40);
		clientTable.setBackground(null);
		bgp.add(clientTable);

		console.setBounds(220,25,580,570);
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

		bgp.setVisible(false);

		//mainwd.setSize(console.getX()+console.getWidth(),mainwd.getHeight());
		mainwd.setVisible(true);

		loginPanel=new LoginPanel(mainwd);
	}
}
