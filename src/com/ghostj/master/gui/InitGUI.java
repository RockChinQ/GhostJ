package com.ghostj.master.gui;

import javax.swing.*;

public class InitGUI {
	public JFrame mainwd=new JFrame();
	public LoginPanel loginPanel;
	public InitGUI(){
		mainwd.setSize(700,700);
		mainwd.setLocation(200,100);
		mainwd.setLayout(null);
		mainwd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainwd.setVisible(true);

		loginPanel=new LoginPanel(mainwd);
	}
}
