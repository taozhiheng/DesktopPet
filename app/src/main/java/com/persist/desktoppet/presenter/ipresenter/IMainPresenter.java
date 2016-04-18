package com.persist.desktoppet.presenter.ipresenter;

/**
 * Created by taozhiheng on 16-4-7.
 *
 * main presenter interface
 */
public interface IMainPresenter {

    void createPet();

    void destroyPet();

    void loadPet();

    void savePet();

    void editPetName(String name);

    void editPetPhrase(String phrase);

    void editConfig();

    void startBluetooth();
}
