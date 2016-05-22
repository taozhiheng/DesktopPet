package com.persist.desktoppet.presenter.ipresenter;

import com.persist.desktoppet.bean.PetBean;

/**
 * Created by taozhiheng on 16-5-22.
 */
public interface IEditPresenter {

    void loadPet();

    void savePet(PetBean petBean);
}
