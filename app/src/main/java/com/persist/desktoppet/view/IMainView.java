package com.persist.desktoppet.view;

import com.persist.desktoppet.bean.PetBean;

/**
 * Created by taozhiheng on 16-4-7.
 *
 * main view interface
 */
public interface IMainView {

    void createPet();

    void destroyPet();

    void loadPet(PetBean pet);

    void editPetName(String name);

    void editPetPhrase(String phrase);

    void editConfig();
}
