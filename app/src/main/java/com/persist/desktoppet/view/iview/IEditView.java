package com.persist.desktoppet.view.iview;

import com.persist.desktoppet.bean.PetBean;

/**
 * Created by taozhiheng on 16-5-22.
 */
public interface IEditView {

    void loadPet(PetBean petBean);

    void savePet(PetBean petBean);
}
