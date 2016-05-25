package com.persist.desktoppet.presenter.impl;

import com.persist.desktoppet.bean.PetBean;
import com.persist.desktoppet.model.imodel.IPetModel;
import com.persist.desktoppet.presenter.ipresenter.IConnectPresenter;
import com.persist.desktoppet.view.iview.IConnectView;

/**
 * Created by taozhiheng on 16-5-25.
 */
public class ConnectPresenterImpl implements IConnectPresenter {

    private IPetModel mModel;
    private IConnectView mView;

    public ConnectPresenterImpl(IPetModel model, IConnectView view)
    {
        if(model == null || view == null)
            throw new IllegalArgumentException("model or view must not be null");
        mModel = model;
        mView = view;
    }

    @Override
    public IPetModel getModel() {
        return mModel;
    }

    @Override
    public void loadIntimatePet() {
        if(mModel.hasIntimatePet())
            mView.loadIntimatePet(mModel.getIntimatePet());
        else
            mView.loadIntimatePet(null);
    }

    @Override
    public void setIntimatePet(PetBean petBean) {
        mModel.setIntimatePet(petBean);
        mView.setIntimatePet(mModel.hasIntimatePet(), mModel.getIntimatePet());
    }

    @Override
    public void removeIntimatePet() {
        mModel.removeIntimatePet();
        mView.removeIntimatePet(!mModel.hasIntimatePet());
    }
}
