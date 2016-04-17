package com.persist.desktoppet;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.persist.desktoppet.model.ConfigModelImpl;
import com.persist.desktoppet.model.IConfigModel;
import com.persist.desktoppet.model.IPetModel;
import com.persist.desktoppet.model.PetModelImpl;
import com.persist.desktoppet.util.LogUtil;

import java.util.List;

/**
 * Created by taozhiheng on 16-4-10.
 *
 * application
 */
public class PetApplication extends Application {

    private final static String TAG = "PetApplication";

    private static IPetModel mPetModel;
    private static IConfigModel mConfigModel;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static IPetModel getPetModel(Context context)
    {
        if(mPetModel == null) {
            mPetModel = new PetModelImpl(context);
            Log.d(TAG, "Process:"+android.os.Process.myPid()+" createModel:"+mPetModel.toString());
        }
        Log.d(TAG, "getModel:"+mPetModel.toString());
        return mPetModel;
    }

    public static IConfigModel getConfigModel(Context context)
    {
        if(mConfigModel == null)
            mConfigModel = new ConfigModelImpl(context);
        return mConfigModel;
    }

    public static boolean isServiceRunning(Context context, String serviceClassName){
        final ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE); //这个value取任意大于1的值，但返回的列表大小可能比这个值小。

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
//            LogUtil.d(TAG, runningServiceInfo.service.getClassName());
            if (runningServiceInfo.service.getClassName().equals(serviceClassName))
            {
                return true;
            }
        }
        return false;
    }
}
