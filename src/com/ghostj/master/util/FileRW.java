package com.ghostj.master.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

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
