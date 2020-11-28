package com.ghostj.server_old;

import com.ghostj.util.FileRW;

import java.io.File;
import java.util.*;

public class TagLog {
	public static class tagOwner{
		public static class tag{
			String name;
			long time=0;
		}
		ArrayList<tag> tags=new ArrayList<>();
		public void addTag(String tagName){
			if (tags.size()>0&&tags.get(tags.size()-1).name.equals(tagName)){//跟上一次一样
				tags.get(tags.size()-1).time=new Date().getTime();
			}else{//不一样或无任何tag
				tag tag=new tag();
				tag.name=tagName;
				tag.time=new Date().getTime();
				tags.add(tag);
			}
		}
	}
	Map<String,tagOwner> allOwner=new LinkedHashMap<>();
	private void addOwner(String indexName){
		allOwner.put(indexName,new tagOwner());
	}
	public void addTag(String ownerName,String tag){
		if(!allOwner.containsKey(ownerName))
			addOwner(ownerName);
		allOwner.get(ownerName).addTag(tag);
	}
	//打包进文件
	public  void  pack(){
		this.smallerPack();
	}
	/*public void pack(){
		JSONObject jsonObject=new JSONObject(true);
		for(String ownerKey:allOwner.keySet()){
			JSONObject aowner=new JSONObject(true);
			tagOwner owner=allOwner.get(ownerKey);
			int index=0;
			for(tagOwner.tag tag:owner.tags){
				JSONObject atag=new JSONObject();
				atag.put("n",tag.name);
				atag.put("t",tag.time);
				aowner.put(""+(index),atag.toJSONString());
				index++;
			}
			jsonObject.put(ownerKey,aowner.toJSONString());
		}
		FileRW.write("tagLog.json",jsonObject.toString());
		smallerPack();
	}*/
	//加载文件
	public void load(){
		smallerLoad();
	}
	/*public void load(){
		allOwner.clear();
		JSONObject jsonObject=JSONObject.parseObject(com.ghostj.util.FileRW.read("tagLog.json"), Feature.OrderedField);
		for(String ownerKey:jsonObject.keySet()){
			JSONObject aowenr=JSONObject.parseObject(jsonObject.getString(ownerKey), Feature.OrderedField);
//			allOwner.put(ownerKey,new tagOwner());
			tagOwner tagOwner=new tagOwner();
			for(String tagKey:aowenr.keySet()){
				JSONObject atag=JSONObject.parseObject(aowenr.getString(tagKey), Feature.OrderedField);
				tagOwner.tag tag=new tagOwner.tag();
				tag.name=atag.getString("n");
				tag.time=atag.getLongValue("t");
				tagOwner.tags.add(tag);
			}
			allOwner.put(ownerKey,tagOwner);
		}
	}*/
	//ownerName:time tag,time2 tag2;ownerName:time tag;
	public void smallerPack(){
		StringBuffer fileStr=new StringBuffer();
		for(String ownerName:allOwner.keySet()){
			StringBuffer aownerStr=new StringBuffer(ownerName+":");
			tagOwner owner=allOwner.get(ownerName);
			int index=0;
			for(tagOwner.tag tag:owner.tags){
				aownerStr.append(tag.time+" "+tag.name);
				aownerStr.append(index==owner.tags.size()-1?"":",");
				index++;
			}
			fileStr.append(aownerStr+";");
		}
		FileRW.write("tagLog.txt",fileStr.toString());
	}
	//ownerName:time tag,time2 tag2;ownerName:time tag;
	public void smallerLoad(){
		allOwner.clear();
		if(!new File("tagLog.txt").exists()){
			return;
		}
		String owners[]=FileRW.read("tagLog.txt").split(";");
		for(String aowner:owners){
			tagOwner tagOwner=new tagOwner();
			String nameAndTags[]=aowner.split(":");
			String tags[]=nameAndTags[1].split(",");
			for(String atag:tags){
				String tagInfo[]=atag.split(" ");
				tagOwner.tag tag=new tagOwner.tag();
				tag.name=tagInfo[1];
				tag.time=Long.parseLong(tagInfo[0]);
				tagOwner.tags.add(tag);
			}
			allOwner.put(nameAndTags[0],tagOwner);
		}
	}
}
