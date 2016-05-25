package com.persist.desktoppet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.persist.desktoppet.util.Const;

import java.util.IllegalFormatCodePointException;

/**
 * Created by taozhiheng on 16-4-7.
 *
 * pet bean
 */
public class PetBean implements Parcelable {

    //the name of the pet
    private String mName;
    //the age of the pet: mAge (days)
    private long mAge;
    //which animal the pet belong to
    private int mType;
    //the sex of the pet, false is male, true is female
    private boolean mSex;
    //the level of the pet, some function require enough level
    private int mLevel;
    //the experience of the current level
    private int mExperience;
    //the pet phrase of the pet
    private String mPhrase;
    //the emotion of the pet
    private int mEmotion;
    //the id of the pet,actually the id is the mac address of the device
    private String mId;
    //whether the pet is alone
    private boolean mAlone;
    //the power the of pet,
    private int mPower;

    public final static int MAX_POWER = 100;

    public PetBean()
    {
        this.mAge = 0;
        this.mLevel = 0;
        this.mEmotion = Const.EMOTION_SMILE;
        this.mAlone = true;
        mPower = 0;
    }

    //value check is needed here.
    public PetBean(String name, long age, int type, boolean sex,
                   int level, int experience, String phrase, int emotion,
                   String id, boolean alone, int power) {
        if(name == null)
            throw new IllegalArgumentException("the name of the pet must not be null");
        this.mName = name;
        if(age < 0)
            age = 0;
        this.mAge = age;
        if(type < 0 || type >= Const.TYPE_NULL)
            type = Const.TYPE_CAT;
        this.mType = type;
        this.mSex = sex;
        if(level < 0)
            level = 0;
        this.mLevel = level;
        if(experience < 0 || experience >= getNeededExperience(level))
            experience = 0;
        this.mExperience = experience;
        this.mPhrase = phrase;
        if(emotion < 0 || emotion >= Const.EMOTION_NULL)
            emotion = Const.EMOTION_SMILE;
        this.mEmotion = emotion;
        this.mId = id;
        this.mAlone = alone;
        this.mPower = power;
    }

    public void setName(String name) {
        if(name != null)
            this.mName = name;
    }

    public void setAge(long age) {
        if(age >= 0)
            this.mAge = age;
    }

    public void setType(int type) {
        if(type >= 0 && type < Const.TYPE_NULL)
            this.mType = type;
    }

    public void setSex(boolean sex) {
        this.mSex = sex;
    }

    //maybe private authority is better
    public void setLevel(int level)
    {
        if(level >= 0)
            this.mLevel = level;
    }

    public void setExperience(int experience)
    {
        //judge whether the level should be reset
        if(experience >= getNeededExperience(mLevel))
        {
            setLevel(mLevel+1);
            this.mExperience = 0;
        }
        else if(experience >= 0)
        {
            this.mExperience = experience;
        }
    }


    public void increaseExperience(int increase)
    {
        if(increase > 0)
            setExperience(mExperience+increase);
    }

    public void setPhrase(String phrase)
    {
        this.mPhrase = phrase;
    }

    public void setEmotion(int emotion)
    {
        if(emotion >= 0 && emotion < Const.EMOTION_NULL)
            this.mEmotion = emotion;
    }

    public void setId(String id)
    {
        if(id != null)
            this.mId = id;
    }

    public void setAlone(boolean alone)
    {
        this.mAlone =  alone;
    }

    public void setPower(int power)
    {
        this.mPower = power;
    }

    public void increasePower(int power)
    {
        mPower += power;
        if(mPower > MAX_POWER)
            mPower = MAX_POWER;
    }

    public void decreasePower(int power)
    {
        if(mPower > power)
            mPower -= power;
        else
            mPower = 0;
    }

    public String getName() {
        return mName;
    }

    public long getAge() {
        return mAge;
    }

    public int getType() {
        return mType;
    }

    public boolean getSex() {
        return mSex;
    }

    public int getLevel()
    {
        return mLevel;
    }

    public int getExperience()
    {
        return mExperience;
    }

    public String getPhrase()
    {
        return mPhrase;
    }

    public int getEmotion()
    {
        return mEmotion;
    }

    public String getId()
    {
        return mId;
    }

    public boolean getAlone()
    {
        return mAlone;
    }

    public int getPower()
    {
        return mPower;
    }

    //calculate the max experience of next level
    public static int getNeededExperience(int level)
    {
        if(level < 0)
            return 0;
        return Const.LEVEL_GAP * (level+1);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeLong(this.mAge);
        dest.writeInt(this.mType);
        dest.writeByte(this.mSex ? (byte) 1 : (byte) 0);
        dest.writeInt(this.mLevel);
        dest.writeInt(this.mExperience);
        dest.writeString(this.mPhrase);
        dest.writeInt(this.mEmotion);
        dest.writeString(this.mId);
        dest.writeByte(this.mAlone ? (byte) 1 : (byte) 0);
        dest.writeInt(this.mPower);
    }

    protected PetBean(Parcel in) {
        this.mName = in.readString();
        this.mAge = in.readLong();
        this.mType = in.readInt();
        this.mSex = in.readByte() != 0;
        this.mLevel = in.readInt();
        this.mExperience = in.readInt();
        this.mPhrase = in.readString();
        this.mEmotion = in.readInt();
        this.mId = in.readString();
        this.mAlone = in.readByte() != 0;
        this.mPower = in.readInt();
    }

    public static final Parcelable.Creator<PetBean> CREATOR = new Parcelable.Creator<PetBean>() {
        @Override
        public PetBean createFromParcel(Parcel source) {
            return new PetBean(source);
        }

        @Override
        public PetBean[] newArray(int size) {
            return new PetBean[size];
        }
    };
}
