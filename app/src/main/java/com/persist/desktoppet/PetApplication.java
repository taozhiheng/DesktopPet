package com.persist.desktoppet;

import android.app.Application;

import com.persist.desktoppet.model.ConfigModelmpl;
import com.persist.desktoppet.model.IConfigModel;
import com.persist.desktoppet.model.IPetModel;
import com.persist.desktoppet.model.PetModelImpl;

/**
 * Created by taozhiheng on 16-4-10.
 *
 * application
 */
public class PetApplication extends Application {

    private static IPetModel mPetModel;
    private static IConfigModel mConfigModel;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static IPetModel getPetModel()
    {
        if(mPetModel == null)
            mPetModel = new PetModelImpl();
        return mPetModel;
    }

    public static IConfigModel getConfigModel()
    {
        if(mConfigModel == null)
            mConfigModel = new ConfigModelmpl();
        return mConfigModel;
    }
}
