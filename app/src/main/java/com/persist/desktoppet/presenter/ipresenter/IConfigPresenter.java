package com.persist.desktoppet.presenter.ipresenter;

import com.persist.desktoppet.bean.ConfigBean;

/**
 * Created by taozhiheng on 16-4-7.
 *
 * config presenter interface
 */
public interface IConfigPresenter {

    void loadConfig();

    void resetConfigTheme(int theme);

    void resetRingConfig(boolean ring);

    void resetVibrateConfig(boolean vibrate);
}
