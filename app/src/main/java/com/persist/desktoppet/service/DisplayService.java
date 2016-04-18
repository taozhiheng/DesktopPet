package com.persist.desktoppet.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.persist.desktoppet.util.Const;
import com.persist.desktoppet.util.LogUtil;
import com.persist.desktoppet.view.window.PetManager;

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
        int cmd;
        if(intent == null)
            cmd = Const.SERVICE_START;
        else
            cmd = intent.getIntExtra(Const.KEY_SERVICE_ACTION, Const.SERVICE_START);
        switch (cmd)
        {
            case Const.SERVICE_START:
                mPetManager.getPresenter().createPet();
                break;
            case Const.SERVICE_RENAME:
                mPetManager.getPresenter().rename(intent.getStringExtra(Const.KEY_NAME));
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        LogUtil.d(TAG, "onDestroy");
        super.onDestroy();
        mPetManager.getPresenter().destroyPet();
        Intent intent = new Intent(Const.KEY_RECEIVER_MAIN);
        sendBroadcast(intent);
    }
}
