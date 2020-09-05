package com.ghostj.client.core;

import com.ghostj.client.func.FuncTest;

/**
 * 客户端启动类
 * @author Rock Chin
 */
public class ClientMain {
    static Processor processor=new Processor();
    public static void main(String[] args){
        processor.registerFunc(new FuncTest());
        //测试
//        processor.start("!test 1 2 3 4 5");
    }
}
