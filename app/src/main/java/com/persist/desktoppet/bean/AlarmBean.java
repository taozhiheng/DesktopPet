package com.persist.desktoppet.bean;


import com.persist.desktoppet.util.AlarmUtil;

/**
 * Created by taozhiheng on 16-2-18.
 * 闹钟信息类，用于记录每个闹钟项的信息:
 * mHour　小时，0－23
 * mMinute　分钟，0－59
 * mTimeDescription　时间字符串描述，('0'+)mHour+':'(+'0')+mMinute
 * mDays　一周的哪几天响起，第0位表示闹钟是否开启响起，第1－7位表示周一至周六是否响起
 * mDaysDescription　哪几天响起的字符串描述，“工作日”、“周末”、“一、二、三”
 * mIsOpen
 */
public class AlarmBean {
    public int mHour;
    public int mMinute;
    public int mDays;
    public String mTimeDescription;
    public String mDaysDescription;

    public AlarmBean()
    {
        this(0, 0, 0);
    }


    public AlarmBean(int hour, int minute, int days)
    {
        this.mHour = hour;
        this.mMinute = minute;
        this.mDays = days;
        this.mTimeDescription = AlarmUtil.getTimeDescription(hour, minute);
        this.mDaysDescription = AlarmUtil.getDaysDescription(days);
    }

    public AlarmBean(long timeMillis, int days)
    {
        int hourAndMin[] = AlarmUtil.getHourAndMinute(timeMillis);
        this.mHour = hourAndMin[0];
        this.mMinute = hourAndMin[1];
        this.mDays = days;
        this.mTimeDescription = AlarmUtil.getTimeDescription(mHour, mMinute);
        this.mDaysDescription = AlarmUtil.getDaysDescription(days);
    }







}
