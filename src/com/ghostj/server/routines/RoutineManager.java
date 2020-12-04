package com.ghostj.server.routines;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 管理所有定时任务
 */
public class RoutineManager {
    HashMap<AbstractRoutine,Boolean> routines=new HashMap<>();
    public RoutineManager registerRoutine(AbstractRoutine routine){
        routines.put(routine,false);
        return this;
    }
    public void startAllRoutine(){
        for(AbstractRoutine routine:routines.keySet()){
            if (!routines.get(routine)){
                routine.start();
                routines.put(routine,true);
            }
        }
    }
}
