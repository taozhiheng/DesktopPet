package com.persist.desktoppet.util;

import android.graphics.Canvas;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by taozhiheng on 15-2-10.
 *
 */
public class CalendarUtils {

    public static Calendar getTimeAfterInSecs(int secs)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, secs);
        return calendar;
    }

    public static Calendar getCurrentTime()
    {
        return Calendar.getInstance();
    }

    public static Calendar getTodayAt(int hours)
    {
        Calendar today = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        return calendar;
    }

    public static long getTimeMillisAsHourAndMinute(long millis)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTimeInMillis();
    }

    public static long getTimeMillisAt(int hour, int minute)
    {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, hour);
        today.set(Calendar.MINUTE, minute);
        return today.getTimeInMillis();
    }

    public static String getDateTimeString(Calendar calendar)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss", Locale.US);
        simpleDateFormat.setLenient(false);
        return simpleDateFormat.format(calendar.getTime());
    }
}
