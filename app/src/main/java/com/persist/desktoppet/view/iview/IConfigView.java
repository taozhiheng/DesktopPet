package com.persist.desktoppet.view.iview;

import com.persist.desktoppet.bean.ConfigBean;

/**
 * Created by taozhiheng on 16-4-7.
 *
 * config view interface
 */
public interface IConfigView {

    void loadConfig(ConfigBean config);

    void resetTheme(int theme);

    void resetRing(boolean ring);

    void resetVibrate(boolean vibrate);
}
