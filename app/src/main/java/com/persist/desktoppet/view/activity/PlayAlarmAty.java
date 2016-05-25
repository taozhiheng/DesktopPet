package com.persist.desktoppet.view.activity;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.persist.desktoppet.R;
import com.persist.desktoppet.receiver.AlarmReceiver;
import com.persist.desktoppet.util.AlarmUtil;


/**
 * Created by nomasp on 2015/10/07.
 */
public class PlayAlarmAty extends Activity {

    private MediaPlayer mp;
    private TextView mShow;
    private Button mStop;
    private PowerManager.WakeLock mWakelock;
    private Vibrator mVibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.alarm_player_aty);

        mShow = (TextView) findViewById(R.id.show_alarm);
        mStop = (Button) findViewById(R.id.stop_alarm);

        mStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String time = getIntent().getStringExtra(AlarmReceiver.TIME);
        if(time == null)
            time = AlarmUtil.getTimeDescription(System.currentTimeMillis());
        mShow.append(time);

        mp = MediaPlayer.create(this,R.raw.music);
        mp.start();
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);//获取震动服务
        long pattern[]={100,400,100,400};//停止  开启  停止  开启
        //控制手机震动的毫秒数，其中第二个参数指定第一个数组参数的索引，-1表示只震动一次，非-1表示从指定下标（第二个参数）开始重复震动
        mVibrator.vibrate(pattern,1);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(mWakelock == null) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "SimpleTimer");
        }
        mWakelock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mWakelock != null)
        {
            mWakelock.release();
            mWakelock = null;
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mp.stop();
        mp.release();
        mp = null;
        mVibrator.cancel();
        mVibrator = null;
    }
}
