package com.ghostj.util;

import java.util.Date;

public class TimeUtil {
    public static String millsToMMDDHHmmSS(long mills){
        Date d=new Date(mills);
        return d.getMonth()+"."+d.getDate()+","+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();
    }
}
