package com.persist.desktoppet.presenter;


import com.persist.desktoppet.model.IPetModel;
import com.persist.desktoppet.view.IMainView;

/**
 * Created by taozhiheng on 16-4-7.
 *
 * main presenter implementation
 */
public class MainPresenterImpl implements IMainPresenter{

    private IPetModel mPetModel;
    private IMainView mMainView;

    public MainPresenterImpl(IPetModel model, IMainView view)
    {
        if(model == null || view == null)
            throw new IllegalArgumentException("model or view must not be null");
        this.mPetModel = model;
        this.mMainView = view;
    }

    @Override
    public void createPet() {
        //model do something
        mMainView.createPet();
    }

    @Override
    public void destroyPet() {
        mMainView.destroyPet();;
    }

    @Override
    public void loadConfig() {
        mMainView.loadConfig();
    }

    @Override
    public void editConfig() {
        mMainView.editConfig();
    }
}
