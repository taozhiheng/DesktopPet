package com.persist.desktoppet.model;

import com.persist.desktoppet.bean.ConfigBean;

/**
 * Created by taozhiheng on 16-4-7.
 *
 * config model interface
 */
public interface IConfigModel {

    void loadConfig();

    ConfigBean getConfig();

    void resetThemeConfig(int theme);

    void resetRingConfig(boolean ring);

    void resetVibrateConfig(boolean vibrate);
}
