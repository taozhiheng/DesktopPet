package com.persist.desktoppet.util;

/**
 * Created by taozhiheng on 16-4-7.
 *
 * constant values set
 */
public class Const {

    //animal types
    public final static int TYPE_CAT = 0;
    public final static int TYPE_DOG = 1;
    public final static int TYPE_BIRD = 2;
    public final static int TYPE_BEAR = 3;
    public final static int TYPE_PENGUIN = 4;
    public final static int TYPE_TIGER = 5;
    public final static int TYPE_LION = 6;
    public final static int TYPE_RABBIT = 7;
    public final static int TYPE_FOX = 8;
    public final static int TYPE_NULL = 9;

    //the gap between two contiguous level
    public final static int LEVEL_GAP = 100;

    //the emotions
    public final static int EMOTION_SMILE = 0;
    public final static int EMOTION_CRY = 1;
    public final static int EMOTION_NULL = 2;

    //sharedPref files
    public final static String PET_PREF = "DesktopPet_pet_preference";
    public final static String CONFIG_PREF = "DeskopPet_config_preference";

    //service intent action
    public final static String DISPLAY_SERVICE_ACTION = "com.persist.desktoppet.service.DisplayService";
    public final static String ALARM_ACTION = "com.persist.desktop.receiver.alarm_trigger_action";

}
