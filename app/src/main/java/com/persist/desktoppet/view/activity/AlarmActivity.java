package com.persist.desktoppet.view.activity;

import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.persist.desktoppet.R;
import com.persist.desktoppet.adapter.AlarmAdapter;
import com.persist.desktoppet.alarm.AlarmInfo;
import com.persist.desktoppet.alarm.AlarmSharedPref;
import com.persist.desktoppet.alarm.Constant;
import com.persist.desktoppet.receiver.AlarmReceiver;
import com.persist.desktoppet.util.AlarmUtil;
import com.persist.desktoppet.util.LogUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmActivity extends BaseActivity {

    private TimePickerDialog mTimePicker;
    private TimePickerDialog mTimeEditPicker;
    private AlertDialog mDayDialog;
    private TextView mDays[] = new TextView[7];
    private int mPosition;
    private RecyclerView mAlarmRecycler;

    private List<AlarmInfo> mAlarmInfoList;
    private AlarmAdapter mAdapter;

    private final static String TAG = "MainActivity";

    public final static String ACTION_ALARM_CLOSE = "com.persist.alarm.alarm_close_action";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtil.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("闹钟");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mAlarmRecycler = (RecyclerView) findViewById(R.id.alarm_recycler);
        mAlarmRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAlarmRecycler.setItemAnimator(new DefaultItemAnimator());
        initDialog();

        mAlarmInfoList = new ArrayList<>();
        mAdapter = new AlarmAdapter(mAlarmInfoList);
        mAdapter.setOnItemTimeClickListener(itemTimeClickListener);
        mAdapter.setOnItemDayClickListener(itemDayClickListener);
        mAdapter.setOnItemSwitchChangedListener(itemSwitchChangedListener);
        mAdapter.setOnItemRemoveListener(itemRemoveListener);
        mAlarmRecycler.setAdapter(mAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTimePicker.show();
                Calendar calendar = Calendar.getInstance();
                mTimePicker.updateTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
            }
        });

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mAlarmInfoList.addAll(AlarmSharedPref.getAlarmInfos(this));
        mAdapter.notifyDataSetChanged();
        setAlarms();
        IntentFilter filter = new IntentFilter(ACTION_ALARM_CLOSE);
        registerReceiver(receiver, filter);
    }

    private void setAlarms()
    {
        if(mAlarmInfoList != null)
        {
            for(AlarmInfo alarmInfo : mAlarmInfoList)
            {
                if(alarmInfo.mIsOpen)
                {
                    AlarmUtil.setAlarmTime(AlarmActivity.this, AlarmUtil.getNextTimeMillis(alarmInfo), Constant.ALARM_ACTION, 0, false);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        LogUtil.d(TAG, "onDestroy");
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private AlarmAdapter.OnItemTimeClickListener itemTimeClickListener = new AlarmAdapter.OnItemTimeClickListener() {
        @Override
        public void onItemTimeClick(TextView v, int position) {
            mPosition = position;
            mTimeEditPicker.show();
            AlarmInfo alarmInfo = mAlarmInfoList.get(position);
            mTimeEditPicker.updateTime(alarmInfo.mHour, alarmInfo.mMinute);
        }
    };

    private AlarmAdapter.OnItemDayClickListener itemDayClickListener = new AlarmAdapter.OnItemDayClickListener() {
        @Override
        public void onItemDayClick(TextView v, int position) {
            mPosition = position;
            for(int i = 0; i < 7; i++) {
                mDays[i].setSelected(false);
                mDayDialog.show();
            }
        }
    };

    private AlarmAdapter.OnItemSwitchChangedListener itemSwitchChangedListener = new AlarmAdapter.OnItemSwitchChangedListener() {
        @Override
        public void onItemSwitchChanged(boolean isChecked, int position) {
            AlarmInfo alarmInfo = mAlarmInfoList.get(position);
            //open or close a alarm item
            //open a alarm item
            if(isChecked)
            {
                alarmInfo.mIsOpen = true;
                AlarmUtil.setAlarmTime(AlarmActivity.this, AlarmUtil.getNextTimeMillis(alarmInfo), Constant.ALARM_ACTION, 0, false);
            }
            //close a alarm item
            else
            {
                AlarmUtil.canalAlarm(AlarmActivity.this, AlarmUtil.getNextTimeMillis(alarmInfo), Constant.ALARM_ACTION);
                alarmInfo.mIsOpen = false;
            }
            //update the data in sharedPreference
            AlarmSharedPref.updateAlarmInfo(AlarmActivity.this, alarmInfo);
        }
    };

    private AlarmAdapter.OnItemRemoveListener itemRemoveListener = new AlarmAdapter.OnItemRemoveListener() {
        @Override
        public void onItemRemove(int position) {
            LogUtil.d(TAG, "remove item at "+position);
            AlarmInfo alarmInfo = mAlarmInfoList.get(position);
            if(AlarmSharedPref.removeAlarmInfo(AlarmActivity.this, alarmInfo))
            {
                if(alarmInfo.mIsOpen)
                    AlarmUtil.canalAlarm(AlarmActivity.this, AlarmUtil.getNextTimeMillis(alarmInfo), Constant.ALARM_ACTION);
                mAlarmInfoList.remove(position);
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount() - position);
            }
        }
    };

    private void initDialog()
    {
        Calendar calendar = Calendar.getInstance();
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {
                AlarmInfo alarmInfo = new AlarmInfo(hourOfDay, minute, 0, true);
                Log.d(TAG, "on time set, "+hourOfDay+":"+minute);
                if(AlarmSharedPref.createAlarmInfo(AlarmActivity.this, alarmInfo))
                {
                    int position = AlarmUtil.addAlarmInfo(mAlarmInfoList, alarmInfo);
                    Log.d(TAG, "create alarm success, position="+position);
                    if(position >= 0)
                    {
                        AlarmUtil.setAlarmTime(AlarmActivity.this, AlarmUtil.getNextTimeMillis(alarmInfo),
                                Constant.ALARM_ACTION, 0, false);
                        mAdapter.notifyItemInserted(position);
                        mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount()-position);
                    }
                }
                else
                {
                    LogUtil.d(TAG, "create alarm fail");
                    Toast.makeText(getBaseContext(), "此时刻闹钟已存在", Toast.LENGTH_SHORT).show();
                }
            }
        }, calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE), true);

        mTimeEditPicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {
                AlarmInfo alarmInfo = mAlarmInfoList.get(mPosition);
                AlarmInfo newAlarmInfo = new AlarmInfo(hourOfDay, minute, alarmInfo.mDays, alarmInfo.mIsOpen);

                if(AlarmSharedPref.resetTimeOfAlarmInfo(AlarmActivity.this, alarmInfo.mTimeDescription, newAlarmInfo))
                {
                    if(alarmInfo.mIsOpen)
                    {
                        AlarmUtil.canalAlarm(AlarmActivity.this, AlarmUtil.getNextTimeMillis(alarmInfo), Constant.ALARM_ACTION);
                        AlarmUtil.setAlarmTime(AlarmActivity.this, AlarmUtil.getNextTimeMillis(newAlarmInfo),
                                Constant.ALARM_ACTION, 0, false);
                    }
                    mAlarmInfoList.remove(mPosition);
                    mAdapter.notifyItemRemoved(mPosition);
                    int position = AlarmUtil.addAlarmInfo(mAlarmInfoList, newAlarmInfo);
                    mAdapter.notifyItemInserted(position);
                    if(mPosition < position)
                        position = mPosition;
                    mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount()-position);
                }
            }
        }, calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE), true);

        View view = LayoutInflater.from(this).inflate(R.layout.day_choice_dialog, null);
        LinearLayout linearLayout = (LinearLayout)view;
        for(int i = 0; i < 7; i++) {
            mDays[i] = (TextView) linearLayout.getChildAt(i);
            mDays[i].setTag(i);
            mDays[i].setOnClickListener(dayOnClickListener);
        }
        mDayDialog = new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int days = 0;
                        for(int i = 0; i < 7; i++)
                        {
                            if(mDays[i].isSelected())
                            {
                                days += 1<<i;
                            }
                        }
                        AlarmInfo alarmInfo = mAlarmInfoList.get(mPosition);
                        if(days != alarmInfo.mDays)
                        {
                            alarmInfo.mDays = days;
                            alarmInfo.mDaysDescription = AlarmUtil.getDaysDescription(days);
                            AlarmSharedPref.updateAlarmInfo(AlarmActivity.this, alarmInfo);
                            mAdapter.notifyItemChanged(mPosition);
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
    }

    private View.OnClickListener dayOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.setSelected(!v.isSelected());
        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String timeKey = intent.getStringExtra(AlarmReceiver.TIME);
            AlarmInfo alarmInfo;
            for(int i = 0; i < mAlarmInfoList.size(); i++)
            {
                alarmInfo = mAlarmInfoList.get(i);
                if(alarmInfo.mTimeDescription.equals(timeKey))
                {
                    alarmInfo.mIsOpen = false;
                    mAdapter.notifyItemChanged(i);
                    return;
                }
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
