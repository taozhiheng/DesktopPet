package com.persist.desktoppet.view.iview;

/**
 * Created by taozhiheng on 16-4-10.
 *
 * display view interface
 */
public interface IDisplayView {
    boolean createPetWindow(int startX, int startY, boolean sex);
    boolean destroyPetWindow();
    boolean dragPetWindow(int newX, int newY);
    boolean switchVisibility(boolean isShow);
    boolean switchEmotion(int emotion);
    boolean rename(String name);
    boolean showMessage(String msg, long duration);
    void switchMovie(int index);
    int getMovieIndex();
    void startRun();
    void stopRun();
}
