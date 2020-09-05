package com.ghostj.client.cmd;

/**
 * 描述了一个逻辑的实现方法
 * @author Rock Chin
 */
public abstract class AbstractFunc {
    public abstract String getFuncName();
    public abstract String[] getParamsModel();
    /**
     * 由派生类来具象化
     * 这个必须是堵塞的执行方法，否则processor将无法hold它
     */
    public abstract void run(String[] params,AbstractProcessor processor);
}
