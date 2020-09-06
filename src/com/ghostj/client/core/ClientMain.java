package com.ghostj.client.core;

import com.ghostj.client.func.FuncDefault;
import com.ghostj.client.func.FuncExit;
import com.ghostj.client.func.FuncProcess;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;

/**
 * 客户端启动类
 * @author Rock Chin
 */
public class ClientMain {
    public static Processor processor=new Processor();
    /**
     * 保存了本客户端的相关信息
     */
    public static String name="";
    public static long sysStartTime=-1;

    /**
     * 客户端入口
     * @param args
     */
    public static void main(String[] args){

        registerAllFunc();
    }

    /**
     * 注册所有func
     */
    private static void registerAllFunc(){
        processor.registerFunc(new FuncProcess());
        processor.registerFunc(new FuncExit());
        processor.setDefaultFunc(new FuncDefault());
    }

    /**
     * 从一个exception对象获取完整的报错信息
     * @param e
     * @return
     */
    public static String getErrorInfo(Exception e){
        StringWriter sw=new StringWriter();
        PrintWriter pw=new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString().replaceAll("\t","    ");
    }


}
