package com.persist.desktoppet.view.window;


import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import com.persist.desktoppet.PetApplication;
import com.persist.desktoppet.R;
import com.persist.desktoppet.presenter.impl.DisplayPresenterImpl;
import com.persist.desktoppet.presenter.ipresenter.IDisplayPresenter;
import com.persist.desktoppet.ui.PetView;
import com.persist.desktoppet.util.Const;
import com.persist.desktoppet.util.LogUtil;
import com.persist.desktoppet.view.iview.IDisplayView;

/**
 * Created by taozhiheng on 16-4-9.
 */
public class PetManager implements IDisplayView {

    private final static String TAG = "PetManager";

    private PetView mPetView;
    private WindowManager.LayoutParams mParams;
    //must be application context, otherwise leak canary may happen
    private Context mContext;
    private IDisplayPresenter mDisplayPresenter;



    private static PetManager mManager;

    public static PetManager newInstance(Context context)
    {
        if(mManager == null)
        {
            mManager = new PetManager(context);
        }
        return mManager;
    }

    public IDisplayPresenter getPresenter()
    {
        return mDisplayPresenter;
    }

    private PetManager(Context context)
    {
        this.mContext = context.getApplicationContext();
        mDisplayPresenter = new DisplayPresenterImpl(
                PetApplication.getPetModel(context), this);
    }


    public boolean createPetWindow()
    {
        if(mPetView != null)
            return false;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        mPetView = new PetView(mContext);
        mPetView.setMovieResource(R.mipmap.gif1);
        mPetView.setOnCloseListener(new PetView.OnCloseListener() {
            @Override
            public void onClose() {
                mDisplayPresenter.destroyPet();
            }
        });
        mPetView.setOnMoveListener(new PetView.OnMoveListener() {
            @Override
            public void onMove(int dx, int dy) {
                dragPetWindow(dx, dy);
            }
        });
        mParams = new WindowManager.LayoutParams();
        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mParams.x = 0;
        mParams.y = 0;
        mParams.gravity = Gravity.LEFT|Gravity.TOP;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //init petView and params
        wm.addView(mPetView, mParams);
        Log.d(TAG, "wm:"+mParams.width+","+mParams.height);
        return true;
    }

    public boolean destroyPetWindow()
    {
        if(mPetView == null)
            return false;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.removeView(mPetView);
        mPetView = null;
        Intent intent = new Intent(Const.ACTION_DISPLAY_SERVICE);
        intent.setPackage(mContext.getPackageName());
        mContext.stopService(intent);
        return true;
    }

    public boolean switchVisibility(boolean isShow)
    {
        if(mPetView == null)
            return false;
        //resize petView
        mPetView.setIsShow(isShow);
        return true;
    }

    @Override
    public boolean dragPetWindow(int newX, int newY) {
        if(mPetView == null)
            return false;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        //reset x, y...
        mParams.x = newX;
        mParams.y = newY;
        LogUtil.d(TAG, "dragPetView, p.x="+mParams.x+", p.y="+mParams.y);
        wm.updateViewLayout(mPetView, mParams);
        return true;
    }

    public boolean switchEmotion(int emotion)
    {
        if(mPetView == null)
            return false;
        mPetView.setEmotion(emotion);
        return true;
    }

    @Override
    public boolean rename(String name) {
        if(mPetView == null || (mPetView.getName() != null && mPetView.getName().equals(name)))
            return false;
        mPetView.setName(name);
        return true;
    }

    public boolean showMessage(String msg, long duration)
    {
        if(mPetView == null || !mPetView.getIsShow())
            return false;
        mPetView.showMessage(msg, duration);
        return true;
    }
}
