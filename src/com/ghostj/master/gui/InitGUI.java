package com.ghostj.master.gui;

import com.ghostj.master.MasterMain;

import javax.swing.*;
import java.awt.*;

public class InitGUI {
	public JFrame mainwd=new JFrame();
	public ClientTable clientTable=new ClientTable();
	public JPanel bgp=new JPanel();

	public JButton testConn=new JButton("Test");

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

		clientTable.setSize(150,this.mainwd.getHeight());
		clientTable.setLocation(10,40);
		clientTable.setBackground(null);
		bgp.add(clientTable);

		bgp.setVisible(false);

		mainwd.setVisible(true);

		loginPanel=new LoginPanel(mainwd);
	}
}
