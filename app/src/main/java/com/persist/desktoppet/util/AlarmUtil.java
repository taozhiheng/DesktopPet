package com.persist.desktoppet.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.persist.desktoppet.alarm.AlarmInfo;
import com.persist.desktoppet.receiver.AlarmReceiver;

import java.util.Calendar;
import java.util.List;

/**
 * Created by taozhiheng on 16-2-18.
 */
public class AlarmUtil {

    private final static String TAG ="AlarmUtil";

    public final static String TIME = "AlarmTime";
    public final static String INTERVAL = "AlarmInterval";
    public final static String REPEAT = "AlarmRepeat";
    public static Calendar calendar = Calendar.getInstance();

    public static void setAlarmTime(Context context, long timeInMillis, String action, int interval, boolean repeat) {
        LogUtil.d(TAG, "setAlarmTime, time="+timeInMillis);
        if(timeInMillis < 0)
            return;
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(action);
        intent.putExtra(TIME, getTimeDescription(timeInMillis));
        intent.putExtra(INTERVAL, interval);
        intent.putExtra(REPEAT, repeat);
        intent.setClass(context, AlarmReceiver.class);
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        int requestCode = (int)(timeInMillis/60/1000);
        PendingIntent sender = PendingIntent.getBroadcast(context, requestCode, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setWindow(AlarmManager.RTC_WAKEUP, timeInMillis, interval, sender);
        } else
        {
            am.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, interval, sender);
        }
        LogUtil.d(TAG, "setAlarmTime, code="+requestCode+", time="+timeInMillis);
    }

    public static void alarmTriggered(Context context, String action, int interval, boolean repeat)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && repeat) {
            setAlarmTime(context, System.currentTimeMillis() + interval, action, interval, true);
        }
    }

    public static void canalAlarm(Context context, long timeInMillis, String action) {
        Intent intent = new Intent(action);
        int requestCode = (int)(timeInMillis/60/1000);
        PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
        LogUtil.d(TAG, "cancelAlarm, code="+requestCode+", time="+timeInMillis);
    }

    /**
     * 获取时间值表示的小时和分钟
     * @param timeMillis the absolute time millis
     * @return the hour and minute values array of the time represent by timeMillis, int[0]-hour,int[1]-minute
     * */
    public static int[] getHourAndMinute(long timeMillis)
    {
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
        if(days == 0)
            return "单次";
        if(days == 0x3e)
            return "工作日";
        if(days == 0x41)
            return "周末";
        if(days == 0x7f)
            return "每天";

        StringBuilder description = new StringBuilder();
        if((days&1) > 0)
            description.append("日 ");
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
            description.append("六");
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

    /**
     * 获得下一次闹钟应该响起的时间
     * 计算时间当时分在当前时间之前时有误
     * @param  alarmInfo 闹钟信息
     * @return the next timeMillis of the alarm item, -1 indicates that the alarm item has been closed
     * */
    public static long getNextTimeMillis(AlarmInfo alarmInfo)
    {
        long timeMillis;
        if(alarmInfo == null || !alarmInfo.mIsOpen) {
            return -1;
        }
        int[] hourAndMin = getHourAndMinute(alarmInfo.mTimeDescription);
        calendar.setTimeInMillis(System.currentTimeMillis());
        Log.d(TAG, "now time is "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));
        calendar.set(Calendar.HOUR_OF_DAY, hourAndMin[0]);
        calendar.set(Calendar.MINUTE, hourAndMin[1]);
        timeMillis = calendar.getTimeInMillis();
        Log.d(TAG, "origin time="+timeMillis);
        //Next time is at today
        if(timeMillis > System.currentTimeMillis())
            return timeMillis;
        //Next time is not at today,get the next time distance about today.
        int day = calendar.get(Calendar.DAY_OF_WEEK)-1;
        int i = 1;
        for(; i < 7; i++)
        {
            if((alarmInfo.mDays&(1<<(day+i))) > 0)
                break;
        }
        //alarmInfo.days == 0
        if(i > 6)
            i = 1;
        return timeMillis+24*60*60*1000*i;
    }

    /**
     * 将alarmInfo按照时间（时和分）从小到大的顺序加入到已有list中
     * @param list the container of alarmInfos
     * @param alarmInfo the alarmInfo that will be added
     * @return the position of the alarmInfo about the list, -1 indicates that the alarmInfo is not added.
     * */
    public static int addAlarmInfo(List<AlarmInfo> list, AlarmInfo alarmInfo)
    {
        if(list == null ||alarmInfo == null)
            return -1;
        int start = 0;
        int end = list.size()-1;
        if(end < 0)
        {
            list.add(alarmInfo);
            return 0;
        }
        int mid;
        AlarmInfo tmp;
        while(start < end)
        {
            mid = (start+end)/2;
            tmp = list.get(mid);
            if(alarmInfo.mHour == tmp.mHour && alarmInfo.mMinute == tmp.mMinute)
            {
                return -1;
            }
            else if(alarmInfo.mHour > tmp.mHour || (alarmInfo.mHour == tmp.mHour && alarmInfo.mMinute > tmp.mMinute))
            {
                start = mid + 1;
            }
            else
            {
                end = mid - 1;
            }
        }
        tmp = list.get(start);
        if(alarmInfo.mHour == tmp.mHour && alarmInfo.mMinute == tmp.mMinute)
        {
            return -1;
        }
        else if(alarmInfo.mHour > tmp.mHour || (alarmInfo.mHour == tmp.mHour && alarmInfo.mMinute > tmp.mMinute))
        {
            list.add(start+1, alarmInfo);
            return start + 1;
        }
        else
        {
            list.add(start, alarmInfo);
            return  start;
        }
    }
}
