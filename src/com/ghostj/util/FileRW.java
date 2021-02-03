package com.ghostj.util;

import java.io.*;

public class FileRW {
    public static String read(String path){
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            StringBuffer result=new StringBuffer();
            String line;
            while ((line=bufferedReader.readLine())!=null){
                result.append(line);
            }
            return result.toString();
        }catch (Exception e){
            return "";
        }
    }
    public static String readMultiLine(String path){
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            StringBuffer result=new StringBuffer();
            String line="";
            while ((line=bufferedReader.readLine())!=null){
                result.append(line+"\n");
            }
            return result.toString();
        }catch (Exception e){
            return "";
        }
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
//            System.out.println("Write file error.");
//            e.printStackTrace();
        }finally {
            try{
                bufferedWriter.close();//
            }catch (Exception e){
//                System.out.println("Close writer failed.");
//                e.printStackTrace();
            }
        }
    }
}
