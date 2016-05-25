package com.persist.desktoppet.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.persist.desktoppet.R;
import com.persist.desktoppet.bean.DeviceBean;
import com.persist.desktoppet.bean.Reply;

import java.util.List;

/**
 * Created by taozhiheng on 16-5-25.
 */
public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ViewHolder>  {


    private List<Reply> mReplies;

    public ReplyAdapter(List<Reply> devices)
    {
        if(devices == null)
            throw new RuntimeException("the device list must not be null!");
        this.mReplies = devices;
    }

    public void addReply(Reply reply)
    {
        mReplies.add(reply);
        notifyItemInserted(mReplies.size()-1);
    }

    @Override
    public int getItemViewType(int position) {
        return mReplies.get(position).type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == Reply.TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reply_user, parent, false);
            return new ViewHolder(view, R.id.imageView_listItem_feedBack_reply_user_portrait,
                    R.id.textView_listItem_feedBack_reply_content);
        }
        else
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reply_client, parent, false);
            return new ViewHolder(view, R.id.imageView_listItem_feedBack_reply_dev_portrait,
                    R.id.textView_listItem_feedBack_reply_content);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reply reply = mReplies.get(position);
        holder.mContent.setText(reply.content);
    }


    @Override
    public int getItemCount() {
        return mReplies.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView mIcon;
        TextView mContent;

        public ViewHolder(View itemView, int iconId, int contentId)
        {
            super(itemView);
            mIcon = (ImageView) itemView.findViewById(iconId);
            mContent = (TextView) itemView.findViewById(contentId);
        }
    }
}
