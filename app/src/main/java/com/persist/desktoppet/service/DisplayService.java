package com.persist.desktoppet.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.persist.desktoppet.PetApplication;
import com.persist.desktoppet.presenter.DisplayPresenterImpl;
import com.persist.desktoppet.presenter.IDisplayPresenter;
import com.persist.desktoppet.util.LogUtil;
import com.persist.desktoppet.view.PetManager;

/**
 * Created by taozhiheng on 16-4-9.
 *
 * display service
 * display, adjust or dismiss desktop pet
 */
public class DisplayService extends Service {

    private PetManager mPetManager;

    private final static String TAG = "DisplayService";

    @Override
    public void onCreate() {
        LogUtil.d(TAG, "onCreate");
        super.onCreate();
        mPetManager = PetManager.newInstance(getApplicationContext());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.d(TAG, "onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(TAG, "onStartCommand");
        mPetManager.getPresenter().createPet();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        LogUtil.d(TAG, "onDestroy");
        super.onDestroy();
        mPetManager.getPresenter().destroyPet();
    }
}
