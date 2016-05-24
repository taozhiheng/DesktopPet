package com.persist.desktoppet.presenter.impl;

import com.persist.desktoppet.model.imodel.IPetModel;
import com.persist.desktoppet.presenter.ipresenter.IFeedPresenter;
import com.persist.desktoppet.view.iview.IEditView;
import com.persist.desktoppet.view.iview.IFeedView;

/**
 * Created by taozhiheng on 16-5-24.
 */
public class FeedPresenterImpl implements IFeedPresenter {

    private IPetModel mPetModel;
    private IFeedView mFeedView;

    public FeedPresenterImpl(IPetModel model, IFeedView view)
    {
        if(model == null || view == null)
            throw new IllegalArgumentException("model or view must not be null");
        this.mPetModel = model;
        this.mFeedView = view;
    }

    @Override
    public void loadPower() {
        mFeedView.loadPower(mPetModel.getPet().getPower());
    }

    @Override
    public void increasePower(int power) {
        mPetModel.increasePetPower(power);
        mFeedView.increasePower(power);
    }
}
