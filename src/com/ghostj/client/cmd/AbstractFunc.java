package com.ghostj.client.cmd;

/**
 * 描述了一个逻辑的实现方法
 * @author Rock Chin
 */
interface AbstractFunc {
    String getFuncName();

    /**
     * 参数列表描述
     * e.g. {"<param0>","<param1>","<param2>"}
     * @return
     */
    String[] getParamsModel();
    String getDescription();
    /**
     * 由派生类来具象化
     * 这个必须是堵塞的执行方法，否则processor将无法hold它
     */
    void run(String[] params,AbstractProcessor processor);
}
