package com.persist.desktoppet.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.persist.desktoppet.alarm.AlarmInfo;
import com.persist.desktoppet.alarm.AlarmSharedPref;
import com.persist.desktoppet.alarm.Constant;
import com.persist.desktoppet.util.AlarmUtil;
import com.persist.desktoppet.util.Const;
import com.persist.desktoppet.view.activity.AlarmActivity;
import com.persist.desktoppet.view.activity.PlayAlarmAty;


/**
 * Created by taozhiheng on 16-2-19.
 *
 * when a alarmItem is triggered, the receiver will receive a broadcast.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private final static String TAG = "AlarmReceiver";
    public final static String TIME = "AlarmTime";

    @Override
    public void onReceive(Context context, Intent intent) {
        String time = intent.getStringExtra(AlarmUtil.TIME);
        if(time == null)
        {
            time = AlarmUtil.getTimeDescription(System.currentTimeMillis());
        }
        Log.d(TAG, "alarm is triggered, time="+time);

        Intent serviceIntent = new Intent(Const.ACTION_DISPLAY_SERVICE);
        serviceIntent.putExtra(Const.KEY_SERVICE_ACTION, Const.SERVICE_ALARM);
        serviceIntent.setPackage(context.getPackageName());
        context.startService(serviceIntent);

        Intent i = new Intent(context, PlayAlarmAty.class);
        i.putExtra(TIME, time);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        AlarmUtil.alarmTriggered(context, intent.getAction(),
                intent.getIntExtra(AlarmUtil.INTERVAL, 0), intent.getBooleanExtra(AlarmUtil.REPEAT, false));

        AlarmInfo alarmInfo = AlarmSharedPref.getAlarmInfo(context, time);
        if(alarmInfo != null)
        {
            //close alarm item
            if (alarmInfo.mDays == 0)
            {
                alarmInfo.mIsOpen = false;
                AlarmSharedPref.updateAlarmInfo(context, alarmInfo);
                Intent  in = new Intent(AlarmActivity.ACTION_ALARM_CLOSE);
                in.putExtra(TIME, time);
                context.sendBroadcast(in);
                Log.d(TAG, "days=0, so close the alarm");
            }
            //repeat alarm
            else
            {
                AlarmUtil.setAlarmTime(context, AlarmUtil.getNextTimeMillis(alarmInfo),
                        Constant.ALARM_ACTION, 0, false);
            }
        }
        else
        {
            Log.d(TAG, "onReceive, the key "+time+" is not exist");
        }
    }
}
