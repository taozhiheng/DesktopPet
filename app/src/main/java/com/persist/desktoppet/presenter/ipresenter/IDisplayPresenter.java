package com.persist.desktoppet.presenter.ipresenter;

import com.persist.desktoppet.model.imodel.IPetModel;
import com.persist.desktoppet.view.iview.IDisplayView;

/**
 * Created by taozhiheng on 16-4-9.
 */
public interface IDisplayPresenter {

    IPetModel getPetModel();
    IDisplayView getDisplayView();

    void createPet();
    void destroyPet(int lastX, int lastY);
    void showPet();
    void hidePet();
    void dragPet(int newX, int newY);
    void switchEmotion(int emotion);
    void switchMovie(int index);
    void rename(String name);
    void showMessage(String msg, long duration);
    void startRun();
    void stopRun();
}
