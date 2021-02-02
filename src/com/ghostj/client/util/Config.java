package com.ghostj.client.util;

import java.io.*;
import java.util.HashMap;

public class Config {
    public HashMap<String,Object> field=new HashMap<>();
    //File configFile;
    String filePath=null;
    public Config(String filePath){
        this.filePath=filePath;
        read();
    }
    public void read(){
        BufferedReader bufferedReader=null;
        try{
            File configFile=new File(this.filePath);
            bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(configFile)));
            String line=null;
            int equalLocation=-1;
            while ((line=bufferedReader.readLine())!=null){
                if((equalLocation=line.indexOf("="))!=-1){//语句正常
                    //截取等号前面作为key，后面作为value
                    this.set(line.substring(0,equalLocation),line.substring(equalLocation+1));
                }
            }
        }catch (Exception e){
            System.out.println("Open config file error.");
            e.printStackTrace();
        }finally {
            try {
                bufferedReader.close();
            }catch (Exception e){
                System.out.println("Close reader failed.");
                e.printStackTrace();
            }
        }
    }
    public void write(){
        BufferedWriter bufferedWriter=null;
        try{
            File configFile=new File(this.filePath);
            bufferedWriter=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile)));
            for(String key:field.keySet()){
                bufferedWriter.write(key+"="+field.get(key));
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        }catch (Exception e){
            System.out.println("Write config file error.");
            e.printStackTrace();
        }finally {
            try{
                bufferedWriter.close();
            }catch (Exception e){
                System.out.println("Close writer failed.");
                e.printStackTrace();
            }
        }
    }


    public String getStringValue(String key){
        return field.get(key).toString();
    }
    public String getStringAnyhow(String key,String def){
        if (field.containsKey(key)){
            return getStringValue(key);
        }else {
            field.put(key,def);
            return def;
        }
    }
    public int getIntValue(String key){
        return Integer.parseInt(field.get(key).toString());
    }
    public int getIntAnyhow(String key,int def){
        if(field.containsKey(key)){
            return getIntValue(key);
        }else {
            field.put(key,def);
            return def;
        }
    }
    public double getDoubleValue(String key){
        return Double.parseDouble(field.get(key).toString());
    }
    public boolean getBooleanValue(String key){
        return Boolean.parseBoolean(field.get(key).toString());
    }
    public char getCharValue(String key){
        return field.get(key).toString().charAt(0);
    }

    public void set(String key,Object value){
        this.field.put(key,value);//相同的key的field应该会替换掉
    }
}
