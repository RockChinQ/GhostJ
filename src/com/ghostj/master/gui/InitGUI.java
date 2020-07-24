package com.ghostj.master.gui;

import com.ghostj.master.MasterMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InitGUI {
	static final Font cf=new Font("微软雅黑",Font.PLAIN,15);
	public JFrame mainwd=new JFrame();
	public ClientTable clientTable=new ClientTable();
	public JPanel bgp=new JPanel();

	public JButton testConn=new JButton("Test");
	public JTextArea console=new JTextArea();
	JScrollPane scrollPane=new JScrollPane();
	public JScrollBar scrollBar=null;
	public JTextField type=new JTextField();

	public LoginPanel loginPanel;
	public InitGUI(){
		mainwd.setSize(800,700);
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

		console.setBounds(220,25,530,570);
		console.setBackground(Color.darkGray);
		console.setForeground(new Color(255, 255, 255, 255));
		console.setFont(cf);
		console.setEditable(false);
		scrollPane.setBounds(console.getBounds());
		scrollPane.setViewportView(console);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollBar=scrollPane.getVerticalScrollBar();

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
						type.setText("");
					}catch (Exception err){
						;
					}
				}
			}
		});
		type.requestFocus();

		bgp.add(type);

		bgp.setVisible(false);

		mainwd.setVisible(true);

		loginPanel=new LoginPanel(mainwd);
	}
}
