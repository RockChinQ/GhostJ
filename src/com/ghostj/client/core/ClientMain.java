package com.ghostj.client.core;

import com.ghostj.client.func.FuncDefault;

/**
 * 客户端启动类
 * @author Rock Chin
 */
public class ClientMain {
    static Processor processor=new Processor();
    public static void main(String[] args){

        registerAllFunc();
    }

    /**
     * 注册所有func
     */
    private static void registerAllFunc(){

        processor.setDefaultFunc(new FuncDefault());
    }
}
