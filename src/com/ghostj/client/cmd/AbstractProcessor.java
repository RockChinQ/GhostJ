package com.ghostj.client.cmd;

import java.util.ArrayList;

/**
 * 定义了命令解码器的模板
 * methods
 * @author Rock Chin
 */
public abstract class AbstractProcessor {
    /**
     * 在匹配一个func时是否忽略大小写
     */
    private boolean ignoreCase=true;
    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }
    /**
     * 保存所有已注册的执行逻辑
     */
    private ArrayList<AbstractFunc> funcs=new ArrayList<>();

    /**
     * 派生类可以通过这个来获取匹配的执行逻辑
     * @param name
     * @return
     */
    private AbstractFunc indexFunc(String name){
        for(AbstractFunc func:funcs){
            if( (ignoreCase&&func.getFuncName().equalsIgnoreCase(name))||(!ignoreCase&&func.getFuncName().equals(name))){
                return func;
            }
        }
        return null;
    }
    /**
     * 注册一个执行逻辑,派生自AbstractFunc的执行逻辑
     * @param func
     */
    public void registerFunc(AbstractFunc func){
        this.funcs.add(func);
    }
    /**
     * 移除一个已注册的执行逻辑
     * @param func
     */
    public void removeFunc(AbstractFunc func){
        this.funcs.remove(func);
    }

    /**
     * 执行一个指令，具体的解码过程将由派生类来实现
     * 这个方法仅解码
     * 仅被start和run方法调用
     * @param cmdStr 完整的命令str
     * @return 返回一个Command对象
     */
    protected abstract Command parse(String cmdStr);

    /**
     * 并行执行一个命令
     * @param cmdStr
     */
    public void start(String cmdStr){

    }

    /**
     * 等待命令执行
     * @param cmdStr
     */
    public void run(String cmdStr){

    }
}
