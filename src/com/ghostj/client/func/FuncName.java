package com.ghostj.client.func;

import com.ghostj.client.cmd.AbstractFunc;
import com.ghostj.client.cmd.AbstractProcessor;
import com.ghostj.client.conn.HandleConn;
import com.ghostj.client.core.ClientMain;

public class FuncName implements AbstractFunc {
    @Override
    public String getFuncName() {
        return "!!name";
    }

    @Override
    public String[] getParamsModel() {
        return new String[]{"<newName>"};
    }

    @Override
    public String getDescription() {
        return "修改客户端名称";
    }

    @Override
    public int getMinParamsAmount() {
        return 1;
    }

    @Override
    public void run(String[] params, String cmd, AbstractProcessor processor) {
        ClientMain.name=params[0];
        ClientMain.getConfig().set("name",params[0]);
        try {
            HandleConn.writeToServer("已修改名称为" + params[0] + "\n");
        }catch (Exception ignored){}
        HandleConn.sendFinishToServer();
    }
}
