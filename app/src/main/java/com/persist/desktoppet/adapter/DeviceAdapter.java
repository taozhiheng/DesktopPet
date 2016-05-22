package com.persist.desktoppet.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.persist.desktoppet.R;
import com.persist.desktoppet.bean.DeviceBean;

import java.util.List;

/**
 * Created by taozhiheng on 16-5-4.
 *
 * device info adapter
 */
public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    private List<DeviceBean> mDevices;
    private OnItemClickListener mListener;

    public interface OnItemClickListener
    {
        void onItemClick(View v, int position, DeviceBean deviceBean);
    }

    public DeviceAdapter(List<DeviceBean> devices)
    {
        if(devices == null)
            throw new RuntimeException("the device list must not be null!");
        this.mDevices = devices;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item, parent, false);
        return new ViewHolder(view, R.id.device_item_name, R.id.device_item_address);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DeviceBean deviceBean = mDevices.get(position);
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(onClickListener);
        holder.mName.setText(deviceBean.name);
        holder.mAddress.setText(deviceBean.address);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            if(mListener != null && position >= 0 && position < mDevices.size())
            {
                mListener.onItemClick(v, position, mDevices.get(position));
            }
        }
    };

    public void setOnItemClickListener(OnItemClickListener l)
    {
        mListener = l;
    }


    @Override
    public int getItemCount() {
        return mDevices.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView mName;
        TextView mAddress;

        public ViewHolder(View itemView, int nameId, int addressId)
        {
            super(itemView);
            mName = (TextView) itemView.findViewById(nameId);
            mAddress = (TextView) itemView.findViewById(addressId);
        }
    }
}
