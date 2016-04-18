package com.persist.desktoppet.model.imodel;

import com.persist.desktoppet.bean.PetBean;

/**
 * Created by taozhiheng on 16-4-7.
 *
 * pet model interface
 */
public interface IPetModel {

    void createPet(PetBean petBean);

    void loadPet();

    void savePet();

    PetBean getPet();

    void updatePetName(String name);

    void increasePetExperience(int increase);

    void updatePetPhrase(String phrase);

    void updatePetEmotion(int emotion);


}
