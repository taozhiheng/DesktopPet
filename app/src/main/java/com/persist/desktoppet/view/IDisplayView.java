package com.persist.desktoppet.view;

/**
 * Created by taozhiheng on 16-4-10.
 *
 * display view interface
 */
public interface IDisplayView {
    boolean createPetWindow();
    boolean destroyPetWindow();
    boolean dragPetWindow(int dx, int dy);
    boolean switchVisibility(boolean isShow);
    boolean switchEmotion(int emotion);
    boolean rename(String name);
    boolean showMessage(String msg, long duration);
}
