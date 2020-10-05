package com.ghostj.server;

import com.ghostj.util.Out;
import com.rft.core.server.FileInfo;
import com.rft.core.server.TaskEvent;

public class FileReceiveEvent implements TaskEvent {
    @Override
    public void taskStarted(String token, FileInfo fileInfo) {
        Out.say("FileReceiveEvent","接收 "+fileInfo.getName()+" pa:"+fileInfo.getSavePath()+" size:"+fileInfo.getSize()+" token:"+token);
        Out.putPrompt();
    }

    @Override
    public void taskFinished(String token, FileInfo fileInfo) {
        Out.say("FileReceiveEvent","成功 "+fileInfo.getName()+" pa:"+fileInfo.getSavePath()+" size:"+fileInfo.getSize()+" token:"+token);
        if(fileInfo.getName().endsWith(".png"))
            Out.say("HandleConn","接收到新图片,url:http://39.100.5.139/ghost/"+ServerMain.fileReceiver.getRootPath()+fileInfo.getSavePath()+"/"+fileInfo.getName());
        Out.putPrompt();
    }

    @Override
    public void taskInterrupted(String token, FileInfo fileInfo) {
        Out.say("FileReceiveEvent","文件被中断 name:"+fileInfo.getName()+" save:"+fileInfo.getSavePath()+" length:"+fileInfo.getSize()+" token:"+token);
        Out.putPrompt();
    }
}
