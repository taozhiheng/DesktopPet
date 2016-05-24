package com.persist.desktoppet.service;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.persist.desktoppet.util.Const;
import com.persist.desktoppet.view.iview.IDisplayView;

import java.lang.ref.WeakReference;

/**
 * Created by taozhiheng on 16-5-24.
 */
public class PowerHandler extends Handler {

    private WeakReference<Context> mContextRef;
    private WeakReference<IDisplayView> mRef;

    public PowerHandler(Context context, IDisplayView view)
    {
        mContextRef = new WeakReference<Context>(context);
        mRef = new WeakReference<IDisplayView>(view);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        IDisplayView displayView = mRef.get();
        if(displayView != null) {
            if (msg.arg1 <= 20)
                displayView.switchMovie(Const.MOVIE_HUNGRY);
            else if (msg.arg1 > 20)
               displayView.switchMovie(Const.MOVIE_NORMAL);
        }
        Context context = mContextRef.get();
        if(context != null) {
            Intent intent = new Intent(Const.KEY_RECEIVER_MAIN);
            intent.putExtra(Const.KEY_RECEIVER_MAIN_ACTION, Const.RECEIVER_ACTION_UPDATE);
            intent.putExtra(Const.KEY_RECEIVER_MAIN_VALUE, msg.arg1);
            context.sendBroadcast(intent);
            intent.setAction(Const.KEY_RECEIVER_FEED);
            intent.putExtra(Const.KEY_RECEIVER_FEED_ACTION, Const.RECEIVER_ACTION_UPDATE);
            intent.putExtra(Const.KEY_RECEIVER_FEED_VALUE, msg.arg1);
            context.sendBroadcast(intent);
        }
    }
}
