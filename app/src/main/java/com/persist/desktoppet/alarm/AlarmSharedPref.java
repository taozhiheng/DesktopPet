package com.persist.desktoppet.alarm;

import android.content.Context;
import android.content.SharedPreferences;

import com.persist.desktoppet.util.AlarmUtil;
import com.persist.desktoppet.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by taozhiheng on 16-2-19.
 * 在sharedPreference中读取闹钟信息
 *
 * 每个闹钟信息用一个键值对存储
 * String - String 00:00 - timeMillis,days,isOpen
 */
public class AlarmSharedPref {

    private final static String TAG = "AlarmSharedPref";
    /**
     * 修改闹钟响起的时间
     * */
    public static boolean resetTimeOfAlarmInfo(Context context, String oldKey, AlarmInfo alarmInfo)
    {
        if(alarmInfo == null || alarmInfo.mTimeDescription.equals(oldKey))
            return false;
        SharedPreferences preferences = context.getSharedPreferences(Constant.SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        //若以前的记录存在
        if(preferences.contains(oldKey))
        {
            editor.remove(oldKey);
        }
        else
        {
            return false;
        }
        //若新记录存在
        if(preferences.contains(alarmInfo.mTimeDescription)) {
            return false;
        }
        editor.putString(alarmInfo.mTimeDescription, alarmInfo.mHour+","+alarmInfo.mMinute+","+alarmInfo.mDays+","+alarmInfo.mIsOpen);
        editor.apply();
        return true;
    }

    /**
     * 修改闹钟响起的日期或开关状态
     * */
    public static boolean updateAlarmInfo(Context context, AlarmInfo alarmInfo)
    {
        if(alarmInfo == null)
            return false;
        SharedPreferences preferences = context.getSharedPreferences(Constant.SHARED_PREF, Context.MODE_PRIVATE);
        if(!preferences.contains(alarmInfo.mTimeDescription))
            return false;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(alarmInfo.mTimeDescription, alarmInfo.mHour+","+alarmInfo.mMinute+","+alarmInfo.mDays+","+alarmInfo.mIsOpen);
        editor.apply();
        LogUtil.d(TAG, "updateAlarmInfo"+preferences.getString(alarmInfo.mTimeDescription, null));
        return true;
    }

    /**
     * 新建一个闹钟记录
     * */
    public static boolean createAlarmInfo(Context context, AlarmInfo alarmInfo)
    {
        if(alarmInfo == null)
            return false;
        SharedPreferences preferences = context.getSharedPreferences(Constant.SHARED_PREF, Context.MODE_PRIVATE);
        if(preferences.contains(alarmInfo.mTimeDescription))
            return false;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(alarmInfo.mTimeDescription, alarmInfo.mHour+","+alarmInfo.mMinute+","+alarmInfo.mDays+","+alarmInfo.mIsOpen);
        editor.apply();
        return true;
    }

    /**
     * 删除一个闹钟记录
     * */
    public static boolean removeAlarmInfo(Context context, AlarmInfo alarmInfo)
    {
        if(alarmInfo == null)
            return false;
        SharedPreferences preferences = context.getSharedPreferences(Constant.SHARED_PREF, Context.MODE_PRIVATE);
        if(!preferences.contains(alarmInfo.mTimeDescription))
            return false;
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(alarmInfo.mTimeDescription);
        editor.apply();
        return true;
    }

    /**
     * 获取指定key的AlarmInfo
     * */
    public static AlarmInfo getAlarmInfo(Context context, String alarmKey)
    {
        SharedPreferences preferences = context.getSharedPreferences(Constant.SHARED_PREF, Context.MODE_PRIVATE);
        String alarmDetail = preferences.getString(alarmKey, null);

        if(alarmDetail != null)
        {
            AlarmInfo alarmInfo = new AlarmInfo();
            alarmInfo.mTimeDescription = alarmKey;
            analysisAlarmInfo(alarmInfo, alarmDetail);
            return alarmInfo;
        }
        else
        {
            return null;
        }
    }

    /**
     * 获取全部有序的闹钟记录
     * @param context
     * @return
     * */
    public static List<AlarmInfo> getAlarmInfos(Context context)
    {
        List<AlarmInfo> alarmInfos = new ArrayList<>();
        SharedPreferences preferences = context.getSharedPreferences(Constant.SHARED_PREF, Context.MODE_PRIVATE);
        Map<String, ?> map =  preferences.getAll();
        AlarmInfo alarmInfo;
        int position;
        for(Map.Entry<String, ?>  entry : map.entrySet())
        {
            LogUtil.d(TAG, "getAlarmInfos, insert item, detail="+entry.getValue().toString());
            alarmInfo = new AlarmInfo();
            alarmInfo.mTimeDescription = entry.getKey();
            analysisAlarmInfo(alarmInfo, (String)entry.getValue());
            AlarmUtil.addAlarmInfo(alarmInfos, alarmInfo);
        }
        return alarmInfos;
    }

    /**
     * 从字符串提取闹钟信息
     * */
    private static void analysisAlarmInfo(AlarmInfo alarmInfo, String infoString)
    {
        String[] infos = infoString.split(",");
        alarmInfo.mHour = Integer.parseInt(infos[0]);
        alarmInfo.mMinute = Integer.parseInt(infos[1]);
        alarmInfo.mDays = Integer.parseInt(infos[2]);
        alarmInfo.mDaysDescription = AlarmUtil.getDaysDescription(alarmInfo.mDays);
        alarmInfo.mIsOpen = Boolean.parseBoolean(infos[3]);
    }

}
