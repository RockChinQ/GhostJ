package com.ghostj.server.core;

import com.ghostj.server.log.Log;
import com.rft.core.server.FileInfo;
import com.rft.core.server.TaskEvent;


public class FileReceiveEvent implements TaskEvent {
    @Override
    public void taskStarted(String token, FileInfo fileInfo) {
//        Out.say("FileReceiveEvent","接收 "+fileInfo.getName()+" pa:"+fileInfo.getSavePath()+" size:"+fileInfo.getSize()+" token:"+token);
        ServerMain.log.puts(Log.NOTIFICATION|Log.INFORMATION,"%CLASS%"
                ,"接收 "+fileInfo.getName()+" pa:"+fileInfo.getSavePath()+" size:"+fileInfo.getSize()+" token:"+token);
    }

    @Override
    public void taskFinished(String token, FileInfo fileInfo) {
        ServerMain.log.puts(Log.NOTIFICATION|Log.INFORMATION,"%CLASS%"
                ,"成功 "+fileInfo.getName()+" pa:"+fileInfo.getSavePath()+" size:"+fileInfo.getSize()+" token:"+token);
//        Out.say("FileReceiveEvent","成功 "+fileInfo.getName()+" pa:"+fileInfo.getSavePath()+" size:"+fileInfo.getSize()+" token:"+token);

//        Out.putPrompt();
    }

    @Override
    public void taskInterrupted(String token, FileInfo fileInfo) {
        ServerMain.log.puts(Log.NOTIFICATION|Log.INFORMATION,"%CLASS%"
                ,"文件被中断 name:"+fileInfo.getName()+" save:"+fileInfo.getSavePath()+" length:"+fileInfo.getSize()+" token:"+token);
    }
}
