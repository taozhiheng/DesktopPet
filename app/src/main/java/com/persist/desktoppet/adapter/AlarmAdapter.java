package com.persist.desktoppet.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.persist.desktoppet.R;
import com.persist.desktoppet.alarm.AlarmInfo;
import com.persist.desktoppet.ui.SwipeLayout;
import com.persist.desktoppet.util.LogUtil;

import java.util.List;


/**
 * Created by taozhiheng on 16-2-18.
 */
public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    private final static String TAG = "AlarmAdapter";

    public interface OnItemTimeClickListener
    {
        void onItemTimeClick(TextView v, int position);
    }

    public interface OnItemDayClickListener
    {
        void onItemDayClick(TextView v, int position);
    }

    public interface OnItemSwitchChangedListener
    {
        void onItemSwitchChanged(boolean isChecked, int position);
    }

    public interface OnItemRemoveListener
    {
        void onItemRemove(int position);
    }


    private List<AlarmInfo> mAlarmInfos;
    private OnItemTimeClickListener mItemTimeListener;
    private OnItemDayClickListener mItemDayListener;
    private OnItemSwitchChangedListener mItemSwitchListener;
    private OnItemRemoveListener mItemRemoveListener;

    public AlarmAdapter(List<AlarmInfo> alarmInfos)
    {
        if(alarmInfos == null)
            throw new IllegalArgumentException("List of alarmInfos is null");
        this.mAlarmInfos = alarmInfos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_item, parent, false);
        return new ViewHolder(view, R.id.item_time, R.id.item_day, R.id.item_switch, R.id.item_remove);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LogUtil.d(TAG, "onBindViewHolder,"+position);
        AlarmInfo alarmInfo = mAlarmInfos.get(position);
        holder.mTime.setText(alarmInfo.mTimeDescription);
        holder.mDay.setText(alarmInfo.mDaysDescription);
        holder.mSwitch.setChecked(alarmInfo.mIsOpen);
        if(holder.itemView instanceof SwipeLayout)
            ((SwipeLayout)holder.itemView).changeDrawerState(SwipeLayout.CLOSE);

        holder.mTime.setTag(position);
        holder.mDay.setTag(position);
        holder.mSwitch.setTag(position);
        holder.mRemove.setTag(position);
        holder.mTime.setOnClickListener(timeListener);
        holder.mDay.setOnClickListener(dayListener);
        holder.mSwitch.setOnCheckedChangeListener(openListener);
        holder.mRemove.setOnClickListener(removeListener);
    }

    private View.OnClickListener timeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mItemTimeListener == null)
                return;
            int position = (Integer) v.getTag();
            mItemTimeListener.onItemTimeClick((TextView) v, position);
        }
    };

    private View.OnClickListener dayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mItemDayListener == null)
                return;
            int position = (Integer) v.getTag();
            mItemDayListener.onItemDayClick((TextView) v, position);
        }
    };


    private CompoundButton.OnCheckedChangeListener openListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(mItemSwitchListener == null)
                return;
            int position = (Integer) buttonView.getTag();
            mItemSwitchListener.onItemSwitchChanged(isChecked, position);
        }
    };

    private View.OnClickListener removeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mItemRemoveListener == null)
                return;
            int position = (Integer) v.getTag();
            mItemRemoveListener.onItemRemove(position);
        }
    };

    @Override
    public int getItemCount() {
        if(mAlarmInfos == null)
            return 0;
        return mAlarmInfos.size();
    }

    public void setOnItemTimeClickListener(OnItemTimeClickListener listener)
    {
        this.mItemTimeListener = listener;
    }

    public void setOnItemDayClickListener(OnItemDayClickListener listener)
    {
        this.mItemDayListener = listener;
    }

    public void setOnItemSwitchChangedListener(OnItemSwitchChangedListener listener)
    {
        this.mItemSwitchListener = listener;
    }

    public void setOnItemRemoveListener(OnItemRemoveListener listener)
    {
        this.mItemRemoveListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView mTime;
        TextView mDay;
        SwitchCompat mSwitch;
        Button mRemove;

        public ViewHolder(View itemView, int timeId, int dayId, int switchId, int removeId)
        {
            super(itemView);
            mTime = (TextView) itemView.findViewById(timeId);
            mDay = (TextView) itemView.findViewById(dayId);
            mSwitch = (SwitchCompat) itemView.findViewById(switchId);
            mRemove = (Button) itemView.findViewById(removeId);
        }
    }
}
