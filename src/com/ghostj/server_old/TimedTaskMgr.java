package com.ghostj.server_old;

import com.ghostj.util.Out;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimedTaskMgr {
    static class Task extends TimerTask{
        long period;
        String cmd;

        Timer proxyTimer;
        @Override
        public void run(){
            try {
                ServerMain.transCmd.handleCommand(cmd);
            }catch (Exception e){
                Out.say("TimedTask","Exception while issue:"+cmd+"\n"+e.getMessage());
                e.printStackTrace();
            }
        }
        public Task(long period,String cmd){
            this.period=period;
            this.cmd=cmd;
            proxyTimer=new Timer();
            proxyTimer.schedule(this,new Date(),period);
        }
        public void stop(){
            proxyTimer.cancel();
        }
    }

    private ArrayList<Task> runningTasks=new ArrayList<>();
    public void addTimedTask(long period,String cmd){
        synchronized (runningTasks) {
            runningTasks.add(new Task(period, cmd));
        }
    }
    public ArrayList<Task> listTasks(){
        return runningTasks;
    }
    public void stop(int index){
        synchronized (runningTasks){
            runningTasks.get(index).stop();
            runningTasks.remove(index);
        }
    }
}
