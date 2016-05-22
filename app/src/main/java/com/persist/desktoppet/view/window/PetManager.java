package com.persist.desktoppet.view.window;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.persist.desktoppet.PetApplication;
import com.persist.desktoppet.R;
import com.persist.desktoppet.presenter.impl.DisplayPresenterImpl;
import com.persist.desktoppet.presenter.ipresenter.IDisplayPresenter;
import com.persist.desktoppet.ui.PetView;
import com.persist.desktoppet.util.Const;
import com.persist.desktoppet.util.LogUtil;
import com.persist.desktoppet.view.iview.IDisplayView;

import java.lang.ref.WeakReference;

/**
 * Created by taozhiheng on 16-4-9.
 */
public class PetManager implements IDisplayView {

    private final static String TAG = "PetManager";

    private PetView mPetView;
    private PetView mIntimatePetView;
    private WindowManager.LayoutParams mParams;
    //must be application context, otherwise leak canary may happen
    private Context mContext;
    private IDisplayPresenter mDisplayPresenter;
    private int mScreenWidth;
    private int mScreenHeight;
    /**
     * the handler to control the pet move randomly
     * */
    private AutoMoveHandler mMoveHandler;
    private AutoMoveRunnable mMoveRunnable;

    private boolean mIsAutoMove = false;


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
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;
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
            public void onMove(int newX, int newY) {
                mDisplayPresenter.dragPet(newX, newY);
            }
        });
        mPetView.setOnDoubleClickListener(new PetView.OnDoubleClickListener() {
            @Override
            public void onDoubleClick() {
                if(!mIsAutoMove)
                    mDisplayPresenter.startRun();
                else
                    mDisplayPresenter.stopRun();
            }
        });
        mParams = new WindowManager.LayoutParams();
        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mParams.x = 0;
        mParams.y = 0;
        mParams.gravity = Gravity.LEFT|Gravity.TOP;
        mParams.width = mScreenHeight/10;
        mParams.height = mScreenHeight/10;
//        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //init petView and params
        wm.addView(mPetView, mParams);
        Log.d(TAG, "wm:"+mParams.width+","+mParams.height);
        return true;
    }

    public boolean destroyPetWindow()
    {
        if(mPetView == null)
            return false;
        stopRun();
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

    @Override
    public void startRun() {
        if(mPetView == null || !mPetView.getIsShow())
            return;
        if(mMoveHandler == null)
            mMoveHandler = new AutoMoveHandler(this);
        if(mMoveRunnable == null)
            mMoveRunnable = new AutoMoveRunnable(mMoveHandler, mScreenWidth/15, mScreenHeight/15);
        mMoveHandler.post(mMoveRunnable);
        mIsAutoMove = true;
    }

    @Override
    public void stopRun() {
        if(mMoveHandler == null)
            return;
        mMoveHandler.removeCallbacks(mMoveRunnable);
        int newX = mScreenWidth - mPetView.getWidth();
        if(mParams.x + mPetView.getWidth()/2 <= mScreenWidth/2)
            newX = 0;
        dragPetWindow(newX, mParams.y);
        mIsAutoMove = false;

    }

    public WindowManager.LayoutParams getPetParams()
    {
        return mParams;
    }


    static class AutoMoveHandler extends Handler
    {
        private WeakReference<PetManager> mPetRef;

        public AutoMoveHandler(PetManager petManager)
        {
            mPetRef = new WeakReference<PetManager>(petManager);
        }

        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0)
            {
                PetManager petManager = mPetRef.get();
                if(petManager != null)
                {
                    WindowManager.LayoutParams params = petManager.getPetParams();
                    int newX = params.x + msg.arg1;
                    int newY = params.y + msg.arg2;
                    petManager.dragPetWindow(newX, newY);
                }
            }
        }
    }

    /**
     * the runnable to calculate the next position of the pet
     * Note:
     * the runnable will run on the UI thread
     * */
    static class AutoMoveRunnable implements Runnable {

        private Handler mHandler;
        private int mMaxX;
        private int mMaxY;

        public AutoMoveRunnable(Handler handler, int maxX, int maxY)
        {
            mHandler = handler;
            mMaxX = maxX;
            mMaxY = maxY;
        }

        @Override
        public void run() {
            int dx = (int)((Math.random()*2-1)*mMaxX);
            int dy = (int)((Math.random()*2-1)*mMaxY);
            Message message = mHandler.obtainMessage(0);
            message.arg1 = dx;
            message.arg2 = dy;
            mHandler.sendMessage(message);
            mHandler.postDelayed(this, 300);
        }
    };
}
