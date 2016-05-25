package com.persist.desktoppet.view.iview;

import com.persist.desktoppet.bean.PetBean;

/**
 * Created by taozhiheng on 16-5-25.
 */
public interface IConnectView {

    void loadIntimatePet(PetBean petBean);

    void setIntimatePet(boolean success, PetBean petBean);

    void removeIntimatePet(boolean success);
}
