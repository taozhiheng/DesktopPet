package com.persist.desktoppet.presenter.impl;

import com.persist.desktoppet.model.imodel.IConfigModel;
import com.persist.desktoppet.presenter.ipresenter.IConfigPresenter;
import com.persist.desktoppet.view.iview.IConfigView;

/**
 * Created by taozhiheng on 16-4-7.
 *
 * config presenter implementation
 */
public class ConfigPresenterImpl implements IConfigPresenter {

    private IConfigModel mConfigModel;
    private IConfigView mConfigView;

    public ConfigPresenterImpl(IConfigModel model, IConfigView view)
    {
        if(model == null || view == null)
            throw new IllegalArgumentException("model or view must not be null");
        this.mConfigModel = model;
        this.mConfigView = view;
    }

    @Override
    public void loadConfig() {
        mConfigModel.loadConfig();
        mConfigView.loadConfig(mConfigModel.getConfig());
    }

    @Override
    public void resetConfigTheme(int theme) {
        mConfigModel.resetThemeConfig(theme);
        mConfigView.resetTheme(theme);
    }

    @Override
    public void resetReceiveConfig(boolean receive) {
        mConfigModel.resetReceiveConfig(receive);
        mConfigView.resetReceiveConfig(receive);
    }

    @Override
    public void resetRingConfig(boolean ring) {
        mConfigModel.resetRingConfig(ring);
        mConfigView.resetRingConfig(ring);
    }

    @Override
    public void resetCareConfig(String care) {
        mConfigModel.resetCareConfig(care);
        mConfigView.resetCareConfig(care);
    }
}
