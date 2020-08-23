package com.ghostj.server;

import com.ghostj.util.FileRW;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class JRERegister {
	/**
	 * 保存每一个jre实体文件信息的类
	 */
	public static class jreFile{
		String fileName=null;
		long version=0;
		public jreFile(String fileName,long version){
			this.fileName=fileName;
			this.version=version;
		}
	}
	ArrayList<jreFile> files=new ArrayList<>();
	public ArrayList<jreFile> getFiles(){
		return files;
	}

	/**
	 * 登记jre文件版本的文件的每个字段
	 */
	private LinkedHashMap<String, Long> fileFields=new LinkedHashMap<>();
	/**
	 * 读取jreVer.txt
	 */
	private void readJREVerTXT(){
		//清空之前的登记添加新的记录
		fileFields.clear();
		if(!new File("jre"+File.separatorChar+"jreVer.txt").exists()){
			FileRW.write("jre"+File.separatorChar+"jreVer.txt","");
			return;
		}
		String verFields[]=FileRW.readMultiLine("jre"+File.separatorChar+"jreVer.txt").split("\n");
		for(String fieldStr:verFields){
			//分割每个版本登记字段
			String spt[]=fieldStr.split(" ");
			if(spt.length>=2)
				fileFields.put(fieldStr.split(" ")[0],Long.parseLong(fieldStr.split(" ")[1]));
		}
	}
	/**
	 * 与文件目录同步
	 */
	public void sync(){
		files.clear();
		readJREVerTXT();
		File jre=new File("jre");
		//不存在jre文件夹
		if(!jre.exists()||!jre.isDirectory()){
			jre.mkdir();
			return;
		}
		scanFile("jre");
	}
	private void scanFile(String dir){
		File files[]=new File(dir).listFiles();
		for(File file:files){
			if(file.isDirectory()){
				scanFile(file.getPath());
			}else if(file.isFile()){
				this.files.add(new jreFile(file.getPath(),fileFields.containsKey(file.getPath())?fileFields.get(file.getPath()):2100000000));
			}
		}
	}
	/**
	 * 将实例中的登记输出到文件
	 */
	public void writeToFile(){
		StringBuffer text=new StringBuffer();
		for(jreFile file:files){
			text.append(file.fileName+" "+file.version+"\n");
		}
		FileRW.write("jre"+File.separatorChar+"jreVer.txt",text.toString());
	}
}