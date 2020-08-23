package com.ghostj.client;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Date;

public class ProcessCmd extends Thread{
    public String cmd="";
    public Process process=null;


    public BufferedWriter processWriter=null;//向process写数据
    public CmdError cmdError=null;

    public String name="";//进程名字，与processlist中的key对应

    public long startTime=0;//此进程创建时间

    public ProcessCmd(String name){
        this.name=name;
        startTime=new Date().getTime();
    }
    public void run(){
        try{
//            ClientMain.processing=true;

            Out.say("ProcessCmd","执行命令");
            process=Runtime.getRuntime().exec(cmd);
            processWriter=new BufferedWriter(new OutputStreamWriter(process.getOutputStream(),"GBK"));

            cmdError=new CmdError(new BufferedReader(new InputStreamReader(process.getErrorStream(),"GBK")));
            cmdError.start();
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
            try {
                ClientMain.bufferedWriter.write("处理命令时出现错误" + ClientMain.getErrorInfo(e));
                ClientMain.bufferedWriter.flush();
            }catch (Exception er){
                ;
            }
        }finally {
            Out.say("ProcessCmd","命令已完成,索引已删除");
            ClientMain.removeProcess(name);
            try {
                ClientMain.bufferedWriter.write("\n命令已完成,process索引已自动删除\n");
                //ClientMain.bufferedWriter.newLine();
                ClientMain.bufferedWriter.flush();
            }catch (Exception e){}
        //            ClientMain.processing=false;
            cmdError.stop();

        }
    }
}
