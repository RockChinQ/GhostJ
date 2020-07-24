package com.ghostj.master.gui;

import com.ghostj.master.MasterMain;

import javax.swing.*;
import java.awt.*;

public class InitGUI {
	public JFrame mainwd=new JFrame();
	public ClientTable clientTable=new ClientTable();
	public JPanel bgp=new JPanel();

	public JButton testConn=new JButton("Test");
	public JTextArea console=new JTextArea();
	JScrollPane scrollPane=new JScrollPane();

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

		console.setBounds(220,25,400,400);
		console.setBackground(Color.darkGray);
		console.setForeground(new Color(0, 214, 232));
		scrollPane.setBounds(console.getBounds());
		scrollPane.setViewportView(console);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		bgp.add(scrollPane);

		bgp.setVisible(false);

		mainwd.setVisible(true);

		loginPanel=new LoginPanel(mainwd);
	}
}
