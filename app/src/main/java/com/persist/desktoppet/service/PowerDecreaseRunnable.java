package com.persist.desktoppet.service;

import com.persist.desktoppet.model.imodel.IPetModel;
import com.persist.desktoppet.util.Const;

import java.lang.ref.WeakReference;

/**
 * Created by taozhiheng on 16-5-24.
 */
public class PowerDecreaseRunnable implements Runnable {

    private WeakReference<IPetModel> mPetModelRef;
    private boolean mContinue;

    public PowerDecreaseRunnable(IPetModel model)
    {
        this.mPetModelRef = new WeakReference<IPetModel>(model);
        mContinue = true;
    }

    @Override
    public void run() {
        while (mContinue) {
            try {
                IPetModel petModel = mPetModelRef.get();
                if(mPetModelRef != null)
                {
                    petModel.decreasePetPower(Const.DECREASE_POWER);
                    Thread.sleep(Const.DECREASE_DURATION);
                }
                else
                    break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void cancel()
    {
        mContinue = false;
    }

    public void reset()
    {
        mContinue = true;
        //???
        Thread.interrupted();
    }
}
