package com.ghostj.client.cmd;

/**
 * 包装一个命令的名字和参数
 * @author Rock Chin
 */
public class Command {
    /**
     * 命令的执行逻辑
     * 指向哪个执行逻辑
     */
    private String funcName;
    /**
     * 此命令的参数
     */
    private String[] params;
    public Command(String name,String[] params){
        this.funcName=name;
        this.params=params;
    }

    public String[] getParams() {
        return params;
    }
    public String getFuncName() {
        return funcName;
    }
}
