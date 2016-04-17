package com.persist.desktoppet.presenter;


import com.persist.desktoppet.model.IPetModel;
import com.persist.desktoppet.util.LogUtil;
import com.persist.desktoppet.view.IMainView;

/**
 * Created by taozhiheng on 16-4-7.
 *
 * main presenter implementation
 */
public class MainPresenterImpl implements IMainPresenter{

    private IPetModel mPetModel;
    private IMainView mMainView;

    private final static String TAG = "MainPresenterImpl";

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
        mMainView.destroyPet();
    }

    @Override
    public void loadPet() {
        mPetModel.loadPet();
        mMainView.loadPet(mPetModel.getPet());
        LogUtil.d(TAG, mPetModel.toString()+"-"+mPetModel.getPet().getName());
    }

    @Override
    public void savePet() {
        mPetModel.savePet();
    }

    @Override
    public void editPetName(String name) {
        mPetModel.updatePetName(name);
        mMainView.editPetName(name);
    }

    @Override
    public void editPetPhrase(String phrase) {
        mPetModel.updatePetPhrase(phrase);
        mMainView.editPetPhrase(phrase);
    }

    @Override
    public void editConfig() {
        mMainView.editConfig();
    }
}
