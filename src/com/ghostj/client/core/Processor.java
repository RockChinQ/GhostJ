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
        String spt[]=cmdStr.split(" ");
        if(spt.length<1){
            return null;
        }
        //检查是否是惊叹号开头
        if(spt[0].startsWith("!")){
            return new Command(spt[0],spt,cmdStr);
        }else {
            return new Command("",spt,cmdStr);
        }
    }
}
