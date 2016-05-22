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

    void updatePet(PetBean petBean);

    void savePet();

    PetBean getPet();

    boolean hasIntimatePet();

    PetBean getIntimatePet();

    void setIntimatePet(PetBean pet);

    void removeIntimatePet();

    void updatePetName(String name);

    void increasePetExperience(int increase);

    void updatePetPhrase(String phrase);

    void updatePetEmotion(int emotion);

    void increasePetPower(int power);

    void decreasePetPower(int power);


    void increaseIntimatePetExperience(int increase);

    void updateIntimatePetEmotion(int emotion);


    void increaseIntimatePetPower(int power);

    void decreaseIntimatePetPower(int power);



}
