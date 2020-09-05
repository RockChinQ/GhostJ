package com.ghostj.client.cmd;

/**
 * 包装一个命令的名字和参数
 * @author Rock Chin
 */
public class Command extends Thread{
    /**
     * 命令的执行逻辑
     * 指向哪个执行逻辑
     */
    private String funcName;
    /**
     * 此命令的参数
     */
    private String[] params;
    /**
     * 此命令的完整内容s
     */
    private String cmd;

    /**
     *
     * @param name 命令名称
     * @param params 参数列表
     * @param cmd 完整命令字符串
     */
    public Command(String name,String[] params,String cmd){
        this.funcName=name;
        this.params=params;
        this.cmd=cmd;
    }

    /**
     * 保存执行逻辑对象，由processor设置
     */
    AbstractFunc func;
    public AbstractFunc getFunc() {
        return func;
    }
    /**
     * processor对象，用于传入执行逻辑
     */
    AbstractProcessor processor;

    public AbstractProcessor getProcessor() {
        return processor;
    }
    /**
     * 设置运行时，由processor执行
     * @param func
     */
    public void setRuntime(AbstractFunc func,AbstractProcessor processor){
        this.func=func;
        this.processor=processor;
    }

    public String[] getParams() {
        return params;
    }
    public String getFuncName() {
        return funcName;
    }

    /**
     * 执行已经设置的func
     */
    @Override
    public void run(){
        func.run(params,cmd,processor);
    }
}
