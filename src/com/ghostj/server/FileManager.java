package com.ghostj.server;

import com.ghostj.util.Out;

import java.io.File;

public class FileManager {
	public static String currentPath="files/";
	private static String httpUrlRoot;
	static {
		httpUrlRoot=ServerMain.config.getStringValue("rft.httpRoot");
	}
	public String getFileListString(){
		StringBuffer result=new StringBuffer("");
		File crtFile=new File(currentPath);
		if (!crtFile.exists()){
			currentPath=".";
			crtFile=new File(currentPath);
		}
		result.append(currentPath);
		File[] fls=crtFile.listFiles();
		for(File fl:fls){
			result.append("|"+fl.getName().replaceAll("!","*"));
			result.append(":"+fl.isDirectory());
			result.append(":"+fl.length());
		}
		return result.toString();
	}

	/**
	 *
	 * @param oneDir 只能提供一层目录名
	 * @return current dr
	 */
	public String changeDir(String oneDir){
		//是..嘛
		if(oneDir.equals("..")){
			String[] spt=currentPath.split("/");
			if (spt.length<1){
				return currentPath;
			}else {
				String re=arrayToString(spt,0,spt.length-1,"/");
				re+=re.endsWith("/")?"":"/";
				currentPath=re;
				return re;
			}
		}else {
			if (new File(currentPath+oneDir).exists()){
				currentPath+=oneDir+(oneDir.endsWith("/")?"":"/");
			}else {
				Out.say("ServerFile","NoSuchDir:"+currentPath+oneDir);
			}
			return currentPath;
		}
	}

	public String arrayToString(String[] arr,int start,int end,String sep){
		StringBuffer result=new StringBuffer();
		for(int i=start;i<end;i++){
			result.append(arr[i]+sep);
		}
		return result.toString();
	}

}
