package com.ghostj.client.core;

import com.ghostj.client.conn.CheckAliveTimer;
import com.ghostj.client.conn.HandleConn;
import com.ghostj.client.func.*;
import com.ghostj.client.util.Config;
import com.ghostj.client.util.FileRW;
import com.ghostj.client.util.Out;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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
     * 连接处理类
     */
    static HandleConn handleConn;

    /**
     * 配置文件
     */
    static Config config;
    public static Config getConfig() {
        return config;
    }
    /**
     * 客户端入口
     * @param args
     */
    public static void main(String[] args){

        sysStartTime=new Date().getTime();
        //加载配置文件
        config=new Config("ghostjc.ini");
        if(!config.field.containsKey("rft.port")){
            config.set("rft.port",1035);
            config.write();
        }
        HandleConn.port=config.getIntValue("port");
        HandleConn.ip=config.getStringValue("ip");
        HandleConn.rft_port=config.getIntValue("rft.port");
        HandleConn.socketAddress=new InetSocketAddress(HandleConn.ip,HandleConn.port);
        name=new String(config.getStringValue("name"));
        Out.say("ClientMain","config load done.port:"+HandleConn.port);
        //启动连接线程
        handleConn=new HandleConn();
        handleConn.start();
        /**
         * 心跳数据计时器
         */
        Timer t=new Timer();
        t.schedule(new CheckAliveTimer(),1000,30000);

        /**
         * daemon计时器
         */
        // FIXME: 2020/9/6 以后要删除,因为离线的daemon已弃用
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                FileRW.write("alive",""+new Date().getTime()/1000);
            }
        },new Date(),(int)(1.5*60*1000));
        registerAllFunc();
    }

    /**
     * 注册所有func
     */
    private static void registerAllFunc(){
        processor.registerFunc(new FuncProcess());
        processor.registerFunc(new FuncExit());
        processor.registerFunc(new FuncSendAlive());
        processor.registerFunc(new FuncRespAlive());
        processor.registerFunc(new FuncGGet());
        processor.registerFunc(new FuncHelp());
        processor.registerFunc(new FuncReconn());
        processor.registerFunc(new FuncConfig());
        processor.registerFunc(new FuncName());
        processor.registerFunc(new FuncBatch());
        processor.registerFunc(new FuncRFT());

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
