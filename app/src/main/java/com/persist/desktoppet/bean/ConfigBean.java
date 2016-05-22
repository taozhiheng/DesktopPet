package com.persist.desktoppet.bean;

import java.util.List;

/**
 * Created by taozhiheng on 16-4-7.
 *
 * app config bean
 */
public class ConfigBean {

    //the current theme of the app, 0 represents boy version, 1 represents girl version
    private int mTheme;
    //whether the app should ring when receiving a message
    private boolean mRing;
    //whether the app should vibrate when receiving a message
    private boolean mVibrate;

    public ConfigBean()
    {

    }

    public ConfigBean(int theme, boolean ring, boolean vibrate) {
        this.mTheme = theme;
        this.mRing = ring;
        this.mVibrate = vibrate;
    }

    public int getThemeConfig()
    {
        return mTheme;
    }

    public boolean getRingConfig()
    {
        return mRing;
    }

    public boolean getVibrateConfig()
    {
        return mVibrate;
    }

    public void setThemeConfig(int theme)
    {
        this.mTheme = theme;
    }

    public void setRingConfig(boolean ring)
    {
        this.mRing = ring;
    }

    public void setVibrateConfig(boolean vibrate)
    {
        this.mVibrate = vibrate;
    }
}
