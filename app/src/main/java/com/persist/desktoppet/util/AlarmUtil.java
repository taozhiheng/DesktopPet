package com.persist.desktoppet.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.persist.desktoppet.bean.AlarmBean;
import com.persist.desktoppet.receiver.AlarmReceiver;

import java.util.Calendar;
import java.util.List;


/**
 * Created by taozhiheng on 16-2-18.
 *
 * should be rewrite
 */
public class AlarmUtil {

    private final static String TAG ="AlarmUtil";

    /**
     * 获取时间值表示的小时和分钟
     * @param timeMillis the absolute time millis
     * @return the hour and minute values array of the time represent by timeMillis, int[0]-hour,int[1]-minute
     * */
    public static int[] getHourAndMinute(long timeMillis)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);
        int hourAndMin[] = new int[2];
        hourAndMin[0] = calendar.get(Calendar.HOUR_OF_DAY);
        hourAndMin[1] = calendar.get(Calendar.MINUTE);
        return hourAndMin;
    }

    /**
     * 获取时间字符串描述的小时和分钟
     * @param timeDescription the time description with hour and minute
     * @return the hour and minute values array of the time represent by timeDescription, int[0]-hour,int[1]-minute
     * */
    public static int[] getHourAndMinute(String timeDescription)
    {
        int hourAndMin[] = new int[2];
        String[] timeSplit = timeDescription.split(":");
        hourAndMin[0] = Integer.parseInt(timeSplit[0]);
        hourAndMin[1] = Integer.parseInt(timeSplit[1]);
        return hourAndMin;
    }

    /**
     * 获取timeMillis所指示时间的字符串描述
     * @param timeMillis the absolute timeMillis of the time
     * @return the description of the time represent by timeMillis
     * */
    public static String getTimeDescription(long timeMillis)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return getTimeDescription(hour, minute);
    }

    /**
     * 获取hour和minute所指示时间的字符串描述
     * @param hour the hourOfDay value(0-23)
     * @param minute the minute value(0-59)
     * @return the description of the time represent by hour and minute
     * */
    public static String getTimeDescription(int hour, int minute)
    {
        StringBuilder time = new StringBuilder(hour+":"+minute);
        if(hour < 10)
            time.insert(0, '0');
        if(minute < 10)
            time.insert(time.indexOf(":")+1, '0');
        return time.toString();
    }

    /**
     * 获取日期编码的字符串描述
     * @param days the code of the days that the alarm should go off
     * @return the description of the days that the alarm should go off
     * */
    public static String getDaysDescription(int days)
    {
        //special days
        switch (days)
        {
            case 0:
            case 1://00000000, 00000001
                return "单次";
            case 0x3e:
            case 0x3f://00111110,00111111
                return "工作日";
            case 0xc0:
            case 0xc1://11000000,11000001
                return "周末";
            case 0xfe:
            case 0xff://11111110,11111111
                return "每天";
        }


        StringBuilder description = new StringBuilder();

        if((days&2) > 0)
            description.append("一 ");
        if((days&4) > 0)
            description.append("二 ");
        if((days&8) > 0)
            description.append("三 ");
        if((days&16) > 0)
            description.append("四 ");
        if((days&32) > 0)
            description.append("五 ");
        if((days&64) > 0)
            description.append("六 ");
        if((days&128) > 0)
            description.append("日");
        else if(description.length() > 1)
            description.deleteCharAt(description.length()-1);
        return description.toString();
    }

    /**
     * 获取日期字符串描述的日期编码
     * @param daysDescription the description of the days that the alarm should go off
     * @return the code of the days that the alarm should go off
     * */
    public static int getDays(String daysDescription)
    {
        if(daysDescription == null || daysDescription.length() <= 0
                || daysDescription.equals("单次"))
            return 0;
        if(daysDescription.equals("工作日"))
            return 0x3e;
        if(daysDescription.equals("周末"))
            return 0x41;
        if(daysDescription.equals("每天"))
            return 0x7f;

        int days = 0;
        if(daysDescription.contains("日"))
            days += 1;
        if(daysDescription.contains("一"))
            days += 2;
        if(daysDescription.contains("二"))
            days += 4;
        if(daysDescription.contains("三"))
            days += 8;
        if(daysDescription.contains("四"))
            days += 16;
        if(daysDescription.contains("五"))
            days += 32;
        if(daysDescription.contains("六"))
            days += 64;
        return days;
    }

    /**
     * 获取此时间字符串相对今天的时间
     * @param timeDescription the description of a time point with hour and minute
     * @return the timeMillis that the time description represents about today
     * */
    public static long getTimeMillis(String timeDescription)
    {
        long timeMillis;
        if(timeDescription == null || !timeDescription.contains(":")) {
            return System.currentTimeMillis();
        }
        String[] times = timeDescription.split(":");
        Calendar calendar = Calendar.getInstance();
        try {
            int hour = Integer.parseInt(times[0]);
            int minute = Integer.parseInt(times[1]);
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        finally {
            timeMillis = calendar.getTimeInMillis();

        }
        return timeMillis;
    }


}
