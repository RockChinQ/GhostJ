package com.ghostj.client.cmd;

/**
 * 自定义类型，描述处理指令时的异常
 * @author Rock Chin
 */
public class CommandProcessException extends Exception{
    public CommandProcessException(){
        super();
    }
    public CommandProcessException(String message){
        super(message);
    }
    public CommandProcessException(Throwable throwable){
        super(throwable);
    }
    public CommandProcessException(String message,Throwable throwable){
        super(message,throwable);
    }
}
