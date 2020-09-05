package com.ghostj.client.func;

import com.ghostj.client.cmd.AbstractFunc;
import com.ghostj.client.cmd.AbstractProcessor;

public class FuncDefault implements AbstractFunc {
    @Override
    public String getFuncName() {
        return "";
    }

    @Override
    public String[] getParamsModel() {
        return new String[0];
    }

    @Override
    public String getDescription() {
        return "直接发送到命令行的指令";
    }

    @Override
    public void run(String[] params, String cmd, AbstractProcessor processor) {

    }
}
