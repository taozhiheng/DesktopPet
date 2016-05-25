package com.persist.desktoppet.presenter.ipresenter;

import com.persist.desktoppet.bean.PetBean;
import com.persist.desktoppet.model.imodel.IPetModel;

/**
 * Created by taozhiheng on 16-5-25.
 */
public interface IConnectPresenter {

    IPetModel getModel();

    void loadIntimatePet();

    void setIntimatePet(PetBean petBean);

    void removeIntimatePet();

}
