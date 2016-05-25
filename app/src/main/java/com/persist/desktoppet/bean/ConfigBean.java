package com.persist.desktoppet.bean;

/**
 * Created by taozhiheng on 16-4-7.
 *
 * app config bean
 */
public class ConfigBean {

    //the current theme of the app, 0 represents boy version, 1 represents girl version
    private int mTheme;
    //whether the app should ring when receiving a message
    private boolean mReceive;
    //whether the app should vibrate when receiving a message
    private boolean mRing;

    public ConfigBean()
    {

    }

    public ConfigBean(int theme, boolean ring, boolean vibrate) {
        this.mTheme = theme;
        this.mReceive = ring;
        this.mRing = vibrate;
    }

    public int getThemeConfig()
    {
        return mTheme;
    }

    public boolean getReceiveConfig()
    {
        return mReceive;
    }

    public boolean getRingConfig()
    {
        return mRing;
    }

    public void setThemeConfig(int theme)
    {
        this.mTheme = theme;
    }

    public void setReceiveConfig(boolean receive)
    {
        this.mReceive = receive;
    }

    public void setRingConfig(boolean ring)
    {
        this.mRing = ring;
    }
}
