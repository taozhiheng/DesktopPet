package com.persist.desktoppet.view;


import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import com.persist.desktoppet.PetApplication;
import com.persist.desktoppet.presenter.DisplayPresenterImpl;
import com.persist.desktoppet.presenter.IDisplayPresenter;
import com.persist.desktoppet.ui.PetView;

/**
 * Created by taozhiheng on 16-4-9.
 */
public class PetManager implements IDisplayView{

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
                PetApplication.getPetModel(), this);
    }


    public boolean createPetWindow()
    {
        if(mPetView != null)
            return false;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        mPetView = new PetView(mContext);
        mPetView.setOnPositionChangeListener(new PetView.OnPositionChangeListener() {
            @Override
            public void onPositionChange(int newX, int newY) {
                mDisplayPresenter.dragPet(newX, newY);
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



    public boolean showMessage(String msg, long duration)
    {
        if(mPetView == null || !mPetView.getIsShow())
            return false;
        mPetView.showMessage(msg, duration);
        return true;
    }
}
