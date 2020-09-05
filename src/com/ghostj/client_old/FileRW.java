package com.ghostj.client_old;

import java.io.*;

public class FileRW {
    public static String read(String path){
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
            StringBuffer result=new StringBuffer();
            String line="";
            while ((line=bufferedReader.readLine())!=null){
                result.append(line);
            }
            return result.toString();
        }catch (Exception e){
            return "";
        }
    }
    public static String readWithLn(String s){
        File f=new File(s);
        try {
            FileInputStream fis=new FileInputStream(f);
            byte[] data=new byte[(int)f.length()];
            fis.read(data);
            fis.close();
            return new String(new String(data));
        } catch (Exception e) {
        }
        return "";
    }
    public static void write(String filePath,String str){
        BufferedWriter bufferedWriter=null;
        try{
            File configFile=new File(filePath);
            bufferedWriter=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile)));
            bufferedWriter.write(str);
            bufferedWriter.flush();
            bufferedWriter.close();
        }catch (Exception e){
            System.out.println("Write file error.");
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
    public static void write(String s0,String s1,boolean b){
        try {
            FileWriter fw=new FileWriter(s0,b);
            fw.write(s1);
            fw.close();
        } catch (Exception e) {
        }
    }
}
