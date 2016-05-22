package com.persist.desktoppet.adapter;

import android.renderscript.Type;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.persist.desktoppet.R;
import com.persist.desktoppet.bean.TypeBean;
import com.persist.desktoppet.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taozhiheng on 16-4-17.
 *
 * animal type adapter
 */
public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.ViewHolder> {


    private final static String TAG = "TypeAdapter";
    private int mSelectedId = 0;

    private List<TypeBean> mList;

    public TypeAdapter(List<TypeBean> list)
    {
        this.mList = new ArrayList<>();
        mList.addAll(list);
    }

    public int getSelectedId()
    {
        return mSelectedId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_item, parent, false);
        return new ViewHolder(view, R.id.item_name, R.id.item_icon);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TypeBean typeBean = mList.get(position);
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(clickListener);
        holder.name.setText(typeBean.name);
        holder.icon.setImageResource(typeBean.iconRes);
        if(position == mSelectedId)
        {
            holder.name.setSelected(true);
            LogUtil.d(TAG, "pos="+position+", selected="+mSelectedId+", "+true);
        }
        else
        {
            holder.name.setSelected(false);
            LogUtil.d(TAG, "pos="+position+", selected="+mSelectedId+", "+false);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = (Integer) v.getTag();
            notifyItemChanged(mSelectedId);
            notifyItemChanged(id);
            mSelectedId = id;
        }
    };

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        ImageView icon;

        public ViewHolder(View view, int nameId, int iconId)
        {
            super(view);
            name = (TextView) view.findViewById(nameId);
            icon = (ImageView) view.findViewById(iconId);
        }
    }
}
