package com.ghostj.server.test;

import com.ghostj.server.core.ServerMain;

public class ControlMsgTest {
	public static void main(String[] args) {
		System.out.println((ServerMain.controlMsgManager.indexByName("rock")==null));
	}
}
