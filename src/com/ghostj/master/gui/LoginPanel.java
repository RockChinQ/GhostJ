package com.ghostj.master.gui;

import com.ghostj.master.MasterMain;
import com.ghostj.master.conn.CreateConn;
import com.ghostj.server_old.ServerMain;

import javax.swing.*;

public class LoginPanel extends JDialog {
	public InputField ipInput=null;
	public InputField portInput=null;
	public InputField nameInput=null;
	public InputField pwInput=null;
	public Button login=new Button("Login");
	public Button exit=new Button("Exit");
	public Button startServer=new Button("启动内建服务端");
	public LoginPanel(JFrame parent){
		super(parent);
		try {
			MasterMain.initGUI.addStringToConsole.addStr("\n============重置连接============\n");
		}catch (Exception e){}
		this.setTitle("Login");
		parent.setEnabled(false);
		this.setSize(450,320);
		this.setLocation(250,200);

		ipInput=new InputField("ip",200,40,40);
		ipInput.setLocation(10,20);
		ipInput.updateCom();

		portInput=new InputField("port",200,40,40);
		portInput.setLocation(10,70);
		portInput.updateCom();

		nameInput=new InputField("name",200,40,40);
		nameInput.setLocation(10,120);
		nameInput.input.setText("root");
		nameInput.updateCom();

		pwInput=new InputField("pw",200,40,40);
		pwInput.setLocation(10,170);
		pwInput.updateCom();


		this.setLayout(null);
		this.add(ipInput);
		this.add(portInput);
		this.add(nameInput);
		this.add(pwInput);

		login.setSize(100,35);
		login.setLocation(10,220);
		this.add(login);
		login.addActionListener((e)->new CreateConn().start());
		exit.setSize(100,35);
		exit.setLocation(120,220);
		this.add(exit);
		exit.addActionListener((e)->System.exit(0));
		startServer.setSize(200,150);
		startServer.setLocation(ipInput.getX()+ipInput.getWidth()+15,ipInput.getY());
		this.add(startServer);
		startServer.addActionListener((e)->{
			if(MasterMain.internalServer==null){
				MasterMain.internalServer=new ServerMain();
				new Thread(()-> ServerMain.main(null)).start();
				ipInput.setValue("localhost");
				startServer.setText("内建服务器正在运行");
			}
		});

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setVisible(true);
		try{
			ipInput.setValue(MasterMain.config.getStringValue("ip"));
			portInput.setValue(MasterMain.config.getStringValue("port"));
			nameInput.setValue(MasterMain.config.getStringValue("name"));
			pwInput.setValue(MasterMain.config.getStringValue("pw"));
		}catch (Exception e){

		}
	}
}
