package com.persist.desktoppet.presenter.impl;

import com.persist.desktoppet.model.imodel.IPetModel;
import com.persist.desktoppet.presenter.ipresenter.IDisplayPresenter;
import com.persist.desktoppet.util.LogUtil;
import com.persist.desktoppet.view.iview.IDisplayView;

/**
 * Created by taozhiheng on 16-4-9.
 *
 * display presenter implementation
 */
public class DisplayPresenterImpl implements IDisplayPresenter {

    private IPetModel mPetModel;
    private IDisplayView mDisplayView;

    private final static String TAG = "DisplayPresenterImpl";

    public DisplayPresenterImpl(IPetModel petModel, IDisplayView displayView)
    {
        if(petModel == null || displayView == null)
            throw new IllegalArgumentException("petModel or displayView must not be null");
        this.mPetModel = petModel;
        this.mDisplayView = displayView;
    }


    @Override
    public void createPet() {
        //model do something
        mDisplayView.createPetWindow();
        mPetModel.loadPet();
        mDisplayView.rename(mPetModel.getPet().getName());
        LogUtil.d(TAG, mPetModel.toString()+"-"+mPetModel.getPet().getName());
    }

    @Override
    public void destroyPet() {
        mDisplayView.destroyPetWindow();
    }

    public void showPet()
    {
        mDisplayView.switchVisibility(true);
    }

    @Override
    public void hidePet() {
        mDisplayView.switchVisibility(false);
    }

    @Override
    public void dragPet(int newX, int newY) {
        mDisplayView.dragPetWindow(newX, newY);
    }

    @Override
    public void switchEmotion(int emotion) {
        mDisplayView.switchEmotion(emotion);
    }

    @Override
    public void rename(String name) {
        mDisplayView.rename(name);
    }

    @Override
    public void showMessage(String msg, long duration) {
        mDisplayView.showMessage(msg, duration);
    }

    @Override
    public void startRun() {
        mDisplayView.startRun();;
    }

    @Override
    public void stopRun() {
        mDisplayView.stopRun();
    }
}
