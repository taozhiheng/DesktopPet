package com.persist.desktoppet.presenter;

/**
 * Created by taozhiheng on 16-4-9.
 */
public interface IDisplayPresenter {

    void createPet();
    void destroyPet();
    void showPet();
    void hidePet();
    void dragPet(int newX, int newY);
    void switchEmotion(int emotion);
    void showMessage(String msg, long duration);
}
