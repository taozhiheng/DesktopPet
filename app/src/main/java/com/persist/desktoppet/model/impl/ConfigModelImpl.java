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
        mConfig.setRingConfig(mSp.getBoolean(Const.KEY_RING, false));
        mConfig.setVibrateConfig(mSp.getBoolean(Const.KEY_VIBRATE, false));
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
    public void resetRingConfig(boolean ring) {
        mConfig.setRingConfig(ring);
        mSp.edit().putBoolean(Const.KEY_RING, ring).apply();
    }

    @Override
    public void resetVibrateConfig(boolean vibrate) {
        mConfig.setVibrateConfig(vibrate);
        mSp.edit().putBoolean(Const.KEY_VIBRATE, vibrate).apply();
    }
}
