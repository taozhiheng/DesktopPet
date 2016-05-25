package com.persist.desktoppet.alarm;


import com.persist.desktoppet.util.AlarmUtil;

/**
 * Created by taozhiheng on 16-2-18.
 * 闹钟信息类，用于记录每个闹钟项的信息:
 * mHour　小时，0－23
 * mMinute　分钟，0－59
 * mTimeDescription　时间字符串描述，('0'+)mHour+':'(+'0')+mMinute
 * mDays　一周的哪几天响起，第０位表示周日是否响起，第1－6位表示周一至周六是否响起
 * mDaysDescription　哪几天响起的字符串描述，“工作日”、“周末”、“一、二、三”
 * mIsOpen
 */
public class AlarmInfo {
    public int mHour;
    public int mMinute;
    public String mTimeDescription;
    public int mDays;
    public String mDaysDescription;
    public boolean mIsOpen;

    public AlarmInfo()
    {
        this(0, 0, 0);
    }

    public AlarmInfo(int hour, int minute, int days)
    {
        this(hour, minute, days, false);
    }

    public AlarmInfo(int hour, int minute, int days, boolean isOpen)
    {
        this.mHour = hour;
        this.mMinute = minute;
        this.mTimeDescription = AlarmUtil.getTimeDescription(hour, minute);
        this.mDays = days;
        this.mDaysDescription = AlarmUtil.getDaysDescription(days);
        this.mIsOpen = isOpen;
    }

    public AlarmInfo(long timeMillis, int days)
    {
        this(timeMillis, days, false);
    }

    public AlarmInfo(long timeMillis, int days, boolean isOpen)
    {
        int hourAndMin[] = AlarmUtil.getHourAndMinute(timeMillis);
        this.mHour = hourAndMin[0];
        this.mMinute = hourAndMin[1];
        this.mTimeDescription = AlarmUtil.getTimeDescription(mHour, mMinute);
        this.mDays = days;
        this.mDaysDescription = AlarmUtil.getDaysDescription(days);
        this.mIsOpen = isOpen;
    }

}
