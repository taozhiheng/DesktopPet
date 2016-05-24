package com.persist.desktoppet.model.imodel;

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

    void resetReceiveConfig(boolean receive);

    void resetRingConfig(boolean ring);
}
