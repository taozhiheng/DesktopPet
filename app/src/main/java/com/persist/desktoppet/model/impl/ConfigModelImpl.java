package com.persist.desktoppet.model.impl;

import android.content.Context;
import android.content.SharedPreferences;

import com.persist.desktoppet.bean.ConfigBean;
import com.persist.desktoppet.model.imodel.IConfigModel;
import com.persist.desktoppet.util.Const;

/**
 * Created by taozhiheng on 16-4-9.
 *
 * config model implementation
 */
public class ConfigModelImpl implements IConfigModel {

    private ConfigBean mConfig;
    private SharedPreferences mSp;

    public ConfigModelImpl(Context context)
    {
        mSp = context.getSharedPreferences(Const.PREF_PET, Context.MODE_PRIVATE);
        mConfig = new ConfigBean();
    }

    @Override
    public void loadConfig() {
        mConfig.setThemeConfig(mSp.getInt(Const.KEY_THEME, 0));
        mConfig.setReceiveConfig(mSp.getBoolean(Const.KEY_RECEIVE, false));
        mConfig.setRingConfig(mSp.getBoolean(Const.KEY_RING, false));
        mConfig.setCareConfig(mSp.getString(Const.KEY_CARE, null));
    }

    @Override
    public ConfigBean getConfig() {
        return mConfig;
    }

    @Override
    public void resetThemeConfig(int theme) {
        mConfig.setThemeConfig(theme);
        mSp.edit().putInt(Const.KEY_THEME, theme).apply();
    }

    @Override
    public void resetReceiveConfig(boolean receive) {
        mConfig.setReceiveConfig(receive);
        mSp.edit().putBoolean(Const.KEY_RECEIVE, receive).apply();
    }

    @Override
    public void resetRingConfig(boolean ring) {
        mConfig.setRingConfig(ring);
        mSp.edit().putBoolean(Const.KEY_RING, ring).apply();
    }

    @Override
    public void resetCareConfig(String care) {
        mConfig.setCareConfig(care);
        mSp.edit().putString(Const.KEY_CARE, care).apply();
    }
}
