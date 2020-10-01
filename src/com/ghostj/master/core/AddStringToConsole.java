package com.ghostj.master.core;

import com.ghostj.master.MasterMain;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class AddStringToConsole {
	public StringBuffer buffer=new StringBuffer();
	public static final int bufferSize=200;
	int countDownTime=maxCountTime;
	static final int maxCountTime=200;
	public AddStringToConsole(){
		new Timer().schedule(new TimerTask(){

			@Override
			public void run() {
				try {
					if (buffer.toString().length()!=0) {
						if (countDownTime > 0) {
							countDownTime -= 20;
						} else {
							countDownTime = maxCountTime;
							flush();
						}
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		},new Date(),20);
	}
	public void addStr(String s){
		buffer.append(s);
		countDownTime=maxCountTime;
		if(buffer.length()>=bufferSize)
			flush();
	}

	public void flush(){
		MasterMain.initGUI.console.setText(MasterMain.initGUI.console.getText()+buffer);
		buffer=new StringBuffer();
		MasterMain.initGUI.console.setCaretPosition(MasterMain.initGUI.console.getText().length());
	}
}
