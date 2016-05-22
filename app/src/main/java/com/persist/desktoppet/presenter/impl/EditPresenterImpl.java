package com.persist.desktoppet.presenter.impl;

import com.persist.desktoppet.bean.PetBean;
import com.persist.desktoppet.model.imodel.IPetModel;
import com.persist.desktoppet.presenter.ipresenter.IEditPresenter;
import com.persist.desktoppet.view.iview.IEditView;

/**
 * Created by taozhiheng on 16-5-22.
 */
public class EditPresenterImpl implements IEditPresenter {

    private IPetModel mPetModel;
    private IEditView mEditView;

    public EditPresenterImpl(IPetModel model, IEditView view)
    {
        if(model == null || view == null)
            throw new IllegalArgumentException("model or view must not be null");
        this.mPetModel = model;
        this.mEditView = view;
    }

    @Override
    public void loadPet() {
        mPetModel.loadPet();
        mEditView.loadPet(mPetModel.getPet());
    }

    @Override
    public void savePet(PetBean petBean) {
        mPetModel.updatePet(petBean);
        mEditView.savePet(petBean);
    }
}
