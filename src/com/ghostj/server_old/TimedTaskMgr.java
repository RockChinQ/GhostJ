package com.ghostj.server_old;

import com.ghostj.util.Out;
import com.ghostj.util.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimedTaskMgr {
    static class Task extends TimerTask{
        long period;
        String cmd;
        String scheTimeStamp;
        long execCount=0,succCount=0;
        private final Timer proxyTimer;
        @Override
        public void run(){
            try {
                ServerMain.transCmd.handleCommand(cmd);
                succCount++;
            }catch (Exception e){
                Out.say("TimedTask","Exception while issue:"+cmd+"\n"+e.getMessage());
                e.printStackTrace();
            }
            execCount++;
        }
        public Task(long period,String cmd){
            this.period=period;
            this.cmd=cmd;
            proxyTimer=new Timer();
            proxyTimer.schedule(this,5000,period);
            scheTimeStamp= TimeUtil.millsToMMDDHHmmSS(new Date().getTime());
        }
        public void stop(){
            proxyTimer.cancel();
        }
    }

    private final ArrayList<Task> runningTasks=new ArrayList<>();
    public void addTimedTask(long period,String cmd){
        synchronized (runningTasks) {
            runningTasks.add(new Task(period, cmd));
        }
    }
    public ArrayList<Task> listTasks(){
        return runningTasks;
    }
    public boolean stop(int index){
        try {
            synchronized (runningTasks) {
                runningTasks.get(index).stop();
                runningTasks.remove(index);
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
