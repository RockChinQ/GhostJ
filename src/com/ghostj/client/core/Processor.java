package com.ghostj.client.core;

import com.ghostj.client.cmd.AbstractProcessor;
import com.ghostj.client.cmd.Command;

/**
 * 用于GhostJ的命令处理器
 * @author Rock Chin
 */
public class Processor extends AbstractProcessor {
    @Override
    protected Command parse(String cmdStr) {
        //检查是否需要删除末尾的惊叹号
        if(cmdStr.endsWith("!")&&cmdStr.startsWith("!")){
            cmdStr=cmdStr.substring(0,cmdStr.length()-1);
        }
        String spt[]=cmdStr.split(" ");
        //如果是空字符则返回null，这将被发送到default func
        if(spt.length<1){
            return null;
        }
        return new Command(spt[0],subArray(spt,1,spt.length),cmdStr);
    }

    /**
     * 取部分数组
     * @param array 原数组
     * @param start 起始位置(包含)
     * @param end 终点位置(不含)
     * @return 截取出的数组
     */
    public static String[] subArray(String[] array,int start,int end){
        String[] result=new String[end-start];
        for(int i=start;i<end;i++){
            result[i-start]=array[i];
        }
        return result;
    }
}
