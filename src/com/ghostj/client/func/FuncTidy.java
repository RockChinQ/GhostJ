package com.ghostj.client.func;

import com.ghostj.client.cmd.AbstractFunc;
import com.ghostj.client.cmd.AbstractProcessor;
import com.ghostj.client.conn.HandleConn;

import java.io.File;

public class FuncTidy implements AbstractFunc {
	private static String[] legalFileName=new String[]{"alive","ghostjc.ini","ghostjc.jar","gl.exe","greg.reg","jreCurVer.txt","jreVer.txt","latestVer.txt","nowVer.txt","rescueip","jre"};
	@Override
	public String getFuncName() {
		return "!!tidy";
	}

	@Override
	public String[] getParamsModel() {
		return new String[0];
	}

	@Override
	public String getDescription() {
		return "整理%GHOSTJ_HOME%";
	}

	@Override
	public int getMinParamsAmount() {
		return 0;
	}

	@Override
	public void run(String[] params, String cmd, AbstractProcessor processor) {
		File wkDir= new File(FuncRFE.homePath);
		File[] fls=wkDir.listFiles();
		for(File fl:fls){
			if(fl.isFile()){
				if(!contains(legalFileName,fl.getName())){
					HandleConn.writeToServerIgnoreException("删除"+fl.getName()+"\n");
					fl.delete();
				}
			}
		}
		HandleConn.writeToServerIgnoreException("整理完成");
	}
	private boolean contains(String[] arr,String obj){
		for(String s:arr){
			if(s.equalsIgnoreCase(obj)){
				return true;
			}
		}
		return false;
	}
}
