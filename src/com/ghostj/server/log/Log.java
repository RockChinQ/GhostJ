package com.ghostj.server.log;

import java.util.ArrayList;

public class Log {
    public final static byte TEXT=0x1,INFO=0x2,WARN=0x4,ERR=0x8;//简单消息(命令执行结果),后台信息,警告,错误
    byte printMode=0xF;//以二进制位来标记要输出的信息从左到右依次是ERR,WARN,INFO,TEXT,默认全输出

    private static class LogEntity{
        String msg;
        byte type;
        LogEntity(byte type,String msg){
            this.type=type;
            this.msg=msg;
        }
    }

    ArrayList<LogEntity> logEntities=new ArrayList<>();
    int printIdx=0;//下一个要输出的位数

    public void printMsg(byte type,String msg){
        logEntities.add(new LogEntity(type,msg));
        if(isValidType(type)){
            printMsg(msg);
        }
    }
    private void printMsg(String msg){
        System.out.println(msg);
        //TODO 添加master定向发报支持
    }

    private boolean isValidType(byte type){
        return (printMode & type) != 0;
    }
    /**
     * 使用位运算操作输出模式开关
     * @param type
     * @param b
     */
    public void setTypeEnable(byte type,boolean b){
        if (b){
            this.printMode= (byte) (this.printMode|type);
        }else{
            this.printMode= (byte) ~((~this.printMode)|type);
        }
    }
}
