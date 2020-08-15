package com.ghostj.server;

import com.ghostj.util.Out;
import com.rft.core.server.FileInfo;
import com.rft.core.server.TaskEvent;

public class FileReceiveEvent implements TaskEvent {
    @Override
    public void taskStarted(String token, FileInfo fileInfo) {
        Out.say("FileReceiveEvent","正在接受收文件 name:"+fileInfo.getName()+" save:"+fileInfo.getSavePath()+" length:"+fileInfo.getSize()+" token:"+token);
        Out.putPrompt();
    }

    @Override
    public void taskFinished(String token, FileInfo fileInfo) {
        Out.say("FileReceiveEvent","文件成功接收 name:"+fileInfo.getName()+" save:"+fileInfo.getSavePath()+" length:"+fileInfo.getSize()+" token:"+token);
        Out.putPrompt();
    }

    @Override
    public void taskInterrupted(String token, FileInfo fileInfo) {
        Out.say("FileReceiveEvent","文件被中断 name:"+fileInfo.getName()+" save:"+fileInfo.getSavePath()+" length:"+fileInfo.getSize()+" token:"+token);
        Out.putPrompt();
    }
}
