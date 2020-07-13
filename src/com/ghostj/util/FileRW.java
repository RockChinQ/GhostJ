package com.ghostj.util;

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
}
