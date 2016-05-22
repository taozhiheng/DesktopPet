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
    private PetBean mIntimatePet;

    public PetModelImpl(Context context)
    {
        mSp = context.getSharedPreferences(Const.PREF_PET, Context.MODE_PRIVATE);
        mPet = new PetBean();
        mIntimatePet = new PetBean();
    }


    @Override
    public void createPet(PetBean petBean) {
        mPet.setName(petBean.getName());
        mPet.setType(petBean.getType());
        mPet.setSex(petBean.getSex());
        mPet.setPhrase(petBean.getPhrase());
        mPet.setId(petBean.getId());
        mPet.setPower(PetBean.MAX_POWER);
        mSp.edit().putBoolean(Const.KEY_IS_FIRST, false).apply();
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
        mPet.setId(mSp.getString(Const.KEY_PET_ID, null));
        mPet.setAlone(mSp.getBoolean(Const.KEY_PET_ALONE, true));
        mPet.setPower(mSp.getInt(Const.KEY_PET_POWER, 0));
        if(!mPet.getAlone())
        {
            loadIntimatePet();
        }
    }

    private void loadIntimatePet()
    {
        mIntimatePet.setName(mSp.getString(Const.KEY_INTIMATE_PET_NAME, null));
        mIntimatePet.setAge(mSp.getLong(Const.KEY_INTIMATE_PET_AGE, 0));
        mIntimatePet.setType(mSp.getInt(Const.KEY_INTIMATE_PET_TYPE, Const.TYPE_CAT));
        mIntimatePet.setSex(mSp.getBoolean(Const.KEY_INTIMATE_PET_SEX, false));
        mIntimatePet.setLevel(mSp.getInt(Const.KEY_INTIMATE_PET_LEVEL, 0));
        mIntimatePet.setExperience(mSp.getInt(Const.KEY_INTIMATE_PET_EXPERIENCE, 0));
        mIntimatePet.setPhrase(mSp.getString(Const.KEY_INTIMATE_PET_PHRASE, null));
        mIntimatePet.setEmotion(mSp.getInt(Const.KEY_INTIMATE_PET_EMOTION, Const.EMOTION_SMILE));
        mIntimatePet.setId(mSp.getString(Const.KEY_INTIMATE_PET_ID, null));
        mIntimatePet.setAlone(mSp.getBoolean(Const.KEY_INTIMATE_PET_ALONE, true));
        mIntimatePet.setPower(mSp.getInt(Const.KEY_INTIMATE_PET_POWER, 0));
    }

    @Override
    public void updatePet(PetBean petBean) {
        mPet.setName(petBean.getName());
        mPet.setSex(petBean.getSex());
        mPet.setPhrase(petBean.getPhrase());
        mSp.edit().putBoolean(Const.KEY_IS_FIRST, false).apply();
        savePet();
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
        editor.putString(Const.KEY_PET_ID, mPet.getId());
        editor.putBoolean(Const.KEY_PET_ALONE, mPet.getAlone());
        editor.putInt(Const.KEY_PET_POWER, mPet.getPower());
        if(!mPet.getAlone())
        {
            saveIntimatePet(editor);
        }
        editor.apply();
    }

    private void saveIntimatePet(SharedPreferences.Editor editor)
    {
        editor.putString(Const.KEY_INTIMATE_PET_NAME, mIntimatePet.getName());
        editor.putLong(Const.KEY_INTIMATE_PET_AGE, mIntimatePet.getAge());
        editor.putInt(Const.KEY_INTIMATE_PET_TYPE, mIntimatePet.getType());
        editor.putBoolean(Const.KEY_INTIMATE_PET_SEX, mIntimatePet.getSex());
        editor.putInt(Const.KEY_INTIMATE_PET_LEVEL, mIntimatePet.getLevel());
        editor.putInt(Const.KEY_INTIMATE_PET_EXPERIENCE, mIntimatePet.getExperience());
        editor.putString(Const.KEY_INTIMATE_PET_PHRASE, mIntimatePet.getPhrase());
        editor.putInt(Const.KEY_INTIMATE_PET_EMOTION, mIntimatePet.getEmotion());
        editor.putString(Const.KEY_INTIMATE_PET_ID, mIntimatePet.getId());
        editor.putBoolean(Const.KEY_INTIMATE_PET_ALONE, mIntimatePet.getAlone());
        editor.putInt(Const.KEY_INTIMATE_PET_POWER, mIntimatePet.getPower());
    }

    @Override
    public PetBean getPet() {
        return mPet;
    }

    @Override
    public boolean hasIntimatePet() {
        return !mPet.getAlone();
    }

    @Override
    public PetBean getIntimatePet() {
        return mIntimatePet;
    }

    @Override
    public void setIntimatePet(PetBean petBean) {
        if(mPet.getAlone())
            return;
        mPet.setAlone(false);
        SharedPreferences.Editor editor = mSp.edit();
        editor.putBoolean(Const.KEY_PET_ALONE, false);
        mIntimatePet.setName(petBean.getName());
        mIntimatePet.setAge(petBean.getAge());
        mIntimatePet.setType(petBean.getType());
        mIntimatePet.setSex(petBean.getSex());
        mIntimatePet.setLevel(petBean.getLevel());
        mIntimatePet.setExperience(petBean.getExperience());
        mIntimatePet.setPhrase(petBean.getPhrase());
        mIntimatePet.setEmotion(petBean.getEmotion());
        mIntimatePet.setId(petBean.getId());
        mIntimatePet.setAlone(false);
        mIntimatePet.setPower(petBean.getPower());
        saveIntimatePet(editor);
        editor.apply();
    }

    @Override
    public void removeIntimatePet() {
        if(mPet.getAlone())
            return;
        mPet.setAlone(true);
        SharedPreferences.Editor editor = mSp.edit();
        editor.putBoolean(Const.KEY_PET_ALONE, true);
        editor.putBoolean(Const.KEY_INTIMATE_PET_ALONE, true);
        editor.apply();
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

    @Override
    public void increasePetPower(int power) {
        mPet.increasePower(power);
        SharedPreferences.Editor editor = mSp.edit();
        editor.putInt(Const.KEY_PET_POWER, mPet.getPower());
        editor.apply();
    }

    @Override
    public void decreasePetPower(int power) {
        mPet.decreasePower(power);
        SharedPreferences.Editor editor = mSp.edit();
        editor.putInt(Const.KEY_PET_POWER, mPet.getPower());
        editor.apply();
    }

    @Override
    public void increaseIntimatePetExperience(int increase) {
        mIntimatePet.increaseExperience(increase);
        SharedPreferences.Editor editor = mSp.edit();
        editor.putInt(Const.KEY_INTIMATE_PET_LEVEL, mIntimatePet.getLevel());
        editor.putInt(Const.KEY_INTIMATE_PET_EXPERIENCE, mIntimatePet.getExperience());
        editor.apply();
    }

    @Override
    public void updateIntimatePetEmotion(int emotion) {
        mIntimatePet.setEmotion(emotion);
        SharedPreferences.Editor editor = mSp.edit();
        editor.putInt(Const.KEY_INTIMATE_PET_EMOTION, emotion);
        editor.apply();
    }

    @Override
    public void increaseIntimatePetPower(int power) {
        mIntimatePet.increasePower(power);
        SharedPreferences.Editor editor = mSp.edit();
        editor.putInt(Const.KEY_INTIMATE_PET_POWER, mIntimatePet.getPower());
        editor.apply();
    }

    @Override
    public void decreaseIntimatePetPower(int power) {
        mIntimatePet.increasePower(power);
        SharedPreferences.Editor editor = mSp.edit();
        editor.putInt(Const.KEY_INTIMATE_PET_POWER, mIntimatePet.getPower());
        editor.apply();
    }
}
