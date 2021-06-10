package com.ghostj.server_old;

import com.ghostj.util.Out;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

/**
 * To tidy the dir.
 * @author Rock Chin
 */
public class SpaceCleaner extends TimerTask {
    private final static long SCRSNF_STORE_PERIOD=1000*60*60*24*14;//14天
    @Override
    public void run(){
        try{
            Date nowDate=new Date();
            long now=nowDate.getTime();
            int year=0;
            Out.say("SpaceCleaner","Cleaning space...");
            //检查跨年日是否在前SSP之内
            Date transYearTimeStamp=new Date(nowDate.getYear(), Calendar.JANUARY,1);
            if (transYearTimeStamp.getTime()+SCRSNF_STORE_PERIOD>now){
                year=nowDate.getYear()-1;
            }else {
                year=nowDate.getYear();
            }
            File scrSnfDir=new File("scrSnf");
            int successDel=0;
            long rlsSpace=0;
            if (scrSnfDir.isDirectory()){
                File[] hostDir=scrSnfDir.listFiles();
                if (hostDir!=null&&hostDir.length>0) {
                    for (File a : hostDir) {
                        if (a.isDirectory()) {
                            File[] scrs = a.listFiles();
                            if (scrs != null && scrs.length > 0) {
                                for (File scr : scrs) {
                                    if (scr.isFile()) {
//                                        System.out.print(scr.getPath()+":");
                                        //解析一个文件名以获取其创建的时间确定是否删除
                                        String[] section = scr.getName().replaceAll("_","-").replaceAll("[.]","-").split("-");
                                        Date d=new Date(year,Integer.parseInt(section[2])-1,Integer.parseInt(section[3])
                                                ,Integer.parseInt(section[4]),Integer.parseInt(section[5]),Integer.parseInt(section[6]));

                                        if (now-d.getTime()>SCRSNF_STORE_PERIOD){
                                            long l=scr.length();
                                            if (scr.delete()){
                                                successDel++;
                                                rlsSpace+=l;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Out.say("SpaceCleaner","deleted count:"+successDel+" rlsSpc(kb):"+rlsSpace/1000);
        }catch (Exception ignored){
            ignored.printStackTrace();
        }
    }
}
