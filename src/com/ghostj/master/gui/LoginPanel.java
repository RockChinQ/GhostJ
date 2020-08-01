package com.ghostj.master.gui;

import com.ghostj.master.MasterMain;
import com.ghostj.master.conn.CreateConn;

import javax.swing.*;

public class LoginPanel extends JDialog {
	public InputField ipInput=null;
	public InputField portInput=null;
	public InputField pwInput=null;
	public Button login=new Button("Login");
	public Button exit=new Button("Exit");
	public LoginPanel(JFrame parent){
		super(parent);
		try {
			MasterMain.initGUI.addStringToConsole.addStr("\n============ÖØÖÃÁ¬½Ó============\n");
		}catch (Exception e){}
		this.setTitle("Login");
		parent.setEnabled(false);
		this.setSize(280,280);
		this.setLocation(250,200);

		ipInput=new InputField("ip",200,40,40);
		ipInput.setLocation(10,20);
		ipInput.updateCom();

		portInput=new InputField("port",200,40,40);
		portInput.setLocation(10,70);
		portInput.updateCom();

		pwInput=new InputField("pw",200,40,40);
		pwInput.setLocation(10,120);
		pwInput.updateCom();

		this.setLayout(null);
		this.add(ipInput);
		this.add(portInput);
		this.add(pwInput);

		login.setSize(100,35);
		login.setLocation(10,170);
		this.add(login);
		login.addActionListener((e)->new CreateConn().start());
		exit.setSize(100,35);
		exit.setLocation(120,170);
		this.add(exit);
		exit.addActionListener((e)->System.exit(0));

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setVisible(true);
		try{
			ipInput.setValue(MasterMain.config.getStringValue("ip"));
			portInput.setValue(MasterMain.config.getStringValue("port"));
			pwInput.setValue(MasterMain.config.getStringValue("pw"));
		}catch (Exception e){

		}
	}
}
