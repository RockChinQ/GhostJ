package com.ghostj.server.routines;

/**
 * 一个定时任务模板
 * name
 * 间隔时间
 * 启动时间
 */
public abstract class AbstractRoutine implements Runnable{
    Thread proxyThread;
    public void start(){
        this.proxyThread=new Thread(this);
        this.proxyThread.start();
    }
    public void stop(){
        this.proxyThread.stop();
    }
    public void startAfter(long mills){
        new Thread(()->{
            try {
                Thread.sleep(mills);
                start();
            }catch (Exception ignored){}
        }).start();
    }
    public abstract void run();
}
