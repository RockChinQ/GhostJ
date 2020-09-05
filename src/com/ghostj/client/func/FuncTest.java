package com.ghostj.client.func;

import com.ghostj.client.cmd.AbstractFunc;
import com.ghostj.client.cmd.AbstractProcessor;

public class FuncTest implements AbstractFunc {
    @Override
    public String getFuncName() {
        return "!test";
    }

    @Override
    public String[] getParamsModel() {
        return new String[0];
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void run(String[] params, String cmd, AbstractProcessor processor) {
        System.out.println("test被call"+"\n参数"+params.length+" "+params.toString()+" cmd"+cmd+" \nprocessorClass:"+processor.getClass());
    }
}
