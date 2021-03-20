package com.ghostj.test;

import com.ghostj.client.core.ClientMain;

import java.io.OutputStreamWriter;
import java.net.Socket;

public class ExitLowExitTest {
	public static void main(String[] args) {
		//建立连接
		try{
			Socket s0=new Socket("localhost",1033);
			OutputStreamWriter w0=new OutputStreamWriter(s0.getOutputStream());
			w0.write("!info test0 c"+ 100
					+" 1 1!");
			w0.flush();
			Socket s1=new Socket("localhost",1033);
			OutputStreamWriter w1=new OutputStreamWriter(s1.getOutputStream());
			w1.write("!info test1 c"+ 101
					+" 1 1!");
			w1.flush();
			Thread.sleep(1000000);
		}catch (Exception e){e.printStackTrace();}
	}
}