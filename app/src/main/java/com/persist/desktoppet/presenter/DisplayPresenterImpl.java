package com.persist.desktoppet.presenter;

import com.persist.desktoppet.model.IPetModel;
import com.persist.desktoppet.view.IDisplayView;

/**
 * Created by taozhiheng on 16-4-9.
 *
 * display presenter implementation
 */
public class DisplayPresenterImpl implements IDisplayPresenter{

    private IPetModel mPetModel;
    private IDisplayView mDisplayView;

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
    public void dragPet(int dx, int dy) {
        mDisplayView.dragPetWindow(dx, dy);
    }

    @Override
    public void switchEmotion(int emotion) {
        mDisplayView.switchEmotion(emotion);
    }

    @Override
    public void showMessage(String msg, long duration) {
        mDisplayView.showMessage(msg, duration);
    }
}
