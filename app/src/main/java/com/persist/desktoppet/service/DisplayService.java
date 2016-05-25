package com.persist.desktoppet.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.persist.desktoppet.PetApplication;
import com.persist.desktoppet.model.PowerChangedListener;
import com.persist.desktoppet.model.imodel.IConfigModel;
import com.persist.desktoppet.model.imodel.IPetModel;
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

    private PowerDecreaseRunnable mDecreaseRunnable;
    private PowerHandler mPowerHandler;

    private final static String TAG = "DisplayService";

    @Override
    public void onCreate() {
        LogUtil.d(TAG, "onCreate");
        super.onCreate();
        mPetManager = PetManager.newInstance(getApplicationContext());
        mPowerHandler = new PowerHandler(getApplicationContext(), mPetManager.getPresenter().getDisplayView());
        IPetModel petModel = mPetManager.getPresenter().getPetModel();
        petModel.setPowerChangedListener(new PowerChangedListener() {
            @Override
            public void onPowerChanged(int power, int oldPower) {
                Log.d(TAG, "power="+power+", old="+oldPower);
                Message msg = mPowerHandler.obtainMessage();
                msg.arg1 = power;
                msg.arg2 = oldPower;
                mPowerHandler.sendMessage(msg);
            }
        });
        mDecreaseRunnable = new PowerDecreaseRunnable(petModel);

        IConfigModel model = PetApplication.getConfigModel(this);
        model.loadConfig();
        boolean listen = model.getConfig().getReceiveConfig();
        Log.d(TAG, "onCreate, listen="+listen);
        if(listen)
        {
            Intent intent = new Intent(this, WXListenService.class);
            intent.setPackage(getPackageName());
            startService(intent);
        }
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
                mDecreaseRunnable.reset();
                new Thread(mDecreaseRunnable).start();
                break;
            case Const.SERVICE_RENAME:
                mPetManager.getPresenter().rename(intent.getStringExtra(Const.KEY_NAME));
                break;
            case Const.SERVICE_ALARM:
                mPetManager.getPresenter().switchMovie(Const.MOVIE_ALARM);
                break;
            case Const.SERVICE_MSG:
                mPetManager.getPresenter().switchMovie(Const.MOVIE_MSG);
                Toast.makeText(getApplicationContext(), intent.getStringExtra(Const.KEY_MSG),
                        Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        LogUtil.d(TAG, "onDestroy");
        super.onDestroy();
        if(mDecreaseRunnable != null)
            mDecreaseRunnable.cancel();
        if(mPowerHandler != null)
            mPowerHandler.removeCallbacksAndMessages(null);
        WindowManager.LayoutParams params = mPetManager.getPetParams();
        mPetManager.getPresenter().destroyPet(params.x, params.y);
        Intent intent = new Intent(Const.KEY_RECEIVER_MAIN);
        sendBroadcast(intent);
    }
}