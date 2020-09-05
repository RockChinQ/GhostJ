package com.ghostj.client.cmd;

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
