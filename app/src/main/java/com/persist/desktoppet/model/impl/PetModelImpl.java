package com.persist.desktoppet.model.impl;

import android.content.Context;
import android.content.SharedPreferences;

import com.persist.desktoppet.bean.PetBean;
import com.persist.desktoppet.model.imodel.IPetModel;
import com.persist.desktoppet.util.Const;

/**
 * Created by taozhiheng on 16-4-9.
 *
 * pet model implementation
 */
public class PetModelImpl implements IPetModel {

    private PetBean mPet;
    private SharedPreferences mSp;

    public PetModelImpl(Context context)
    {
        mSp = context.getSharedPreferences(Const.PREF_PET, Context.MODE_PRIVATE);
        mPet = new PetBean();
    }


    @Override
    public void createPet(PetBean petBean) {
        mPet.setName(petBean.getName());
        mPet.setType(petBean.getType());
        mPet.setSex(petBean.getSex());
        mPet.setPhrase(petBean.getPhrase());
        savePet();
    }

    @Override
    public void loadPet() {
        mPet.setName(mSp.getString(Const.KEY_PET_NAME, null));
        mPet.setAge(mSp.getLong(Const.KEY_PET_AGE, 0));
        mPet.setType(mSp.getInt(Const.KEY_PET_TYPE, Const.TYPE_CAT));
        mPet.setSex(mSp.getBoolean(Const.KEY_PET_SEX, false));
        mPet.setLevel(mSp.getInt(Const.KEY_PET_LEVEL, 0));
        mPet.setExperience(mSp.getInt(Const.KEY_PET_EXPERIENCE, 0));
        mPet.setPhrase(mSp.getString(Const.KEY_PET_PHRASE, null));
        mPet.setEmotion(mSp.getInt(Const.KEY_PET_EMOTION, Const.EMOTION_SMILE));
    }

    @Override
    public void savePet() {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putString(Const.KEY_PET_NAME, mPet.getName());
        editor.putLong(Const.KEY_PET_AGE, mPet.getAge());
        editor.putInt(Const.KEY_PET_TYPE, mPet.getType());
        editor.putBoolean(Const.KEY_PET_SEX, mPet.getSex());
        editor.putInt(Const.KEY_PET_LEVEL, mPet.getLevel());
        editor.putInt(Const.KEY_PET_EXPERIENCE, mPet.getExperience());
        editor.putString(Const.KEY_PET_PHRASE, mPet.getPhrase());
        editor.putInt(Const.KEY_PET_EMOTION, mPet.getEmotion());
        editor.apply();
    }

    @Override
    public PetBean getPet() {
        return mPet;
    }



    @Override
    public void updatePetName(String name) {
        mPet.setName(name);
        SharedPreferences.Editor editor = mSp.edit();
        editor.putString(Const.KEY_PET_NAME, mPet.getName());
        editor.apply();
    }

    @Override
    public void increasePetExperience(int increase) {
        mPet.increaseExperience(increase);
        SharedPreferences.Editor editor = mSp.edit();
        editor.putInt(Const.KEY_PET_LEVEL, mPet.getLevel());
        editor.putInt(Const.KEY_PET_EXPERIENCE, mPet.getExperience());
        editor.apply();
    }


    @Override
    public void updatePetPhrase(String phrase) {
        mPet.setPhrase(phrase);
        SharedPreferences.Editor editor = mSp.edit();
        editor.putString(Const.KEY_PET_PHRASE, mPet.getPhrase());
        editor.apply();
    }

    public void updatePetEmotion(int emotion)
    {
        mPet.setEmotion(emotion);
        SharedPreferences.Editor editor = mSp.edit();
        editor.putInt(Const.KEY_PET_EMOTION, emotion);
        editor.apply();
    }
}
