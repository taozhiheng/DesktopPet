package com.persist.desktoppet.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.persist.desktoppet.PetApplication;
import com.persist.desktoppet.presenter.DisplayPresenterImpl;
import com.persist.desktoppet.presenter.IDisplayPresenter;
import com.persist.desktoppet.view.PetManager;

/**
 * Created by taozhiheng on 16-4-9.
 *
 * display service
 * display, adjust or dismiss desktop pet
 */
public class DisplayService extends Service {

    private IDisplayPresenter mDisplayPresenter;

    @Override
    public void onCreate() {
        super.onCreate();
        mDisplayPresenter = new DisplayPresenterImpl(
                PetApplication.getPetModel(),
                PetManager.newInstance(getApplicationContext()));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mDisplayPresenter.createPet();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisplayPresenter.destroyPet();
    }
}
