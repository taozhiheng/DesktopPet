package com.persist.desktoppet.util;

import com.persist.desktoppet.R;

import java.util.UUID;

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

    //sharedPref file
    public final static String PREF_PET = "DesktopPet_preference_pet";

    //service intent action
    public final static String ACTION_DISPLAY_SERVICE = "com.persist.desktoppet.service.DisplayService";
    public final static String ACTION_ALARM = "com.persist.desktop.receiver.alarm_trigger_action";

    //data key
    public final static String KEY_PET_NAME = "DesktopPet_key_pet_name";
    public final static String KEY_PET_AGE = "DesktopPet_key_pet_age";
    public final static String KEY_PET_TYPE = "DesktopPet_key_pet_type";
    public final static String KEY_PET_SEX = "DesktopPet_key_pet_sex";
    public final static String KEY_PET_LEVEL = "DesktopPet_key_pet_level";
    public final static String KEY_PET_EXPERIENCE = "DesktopPet_key_pet_experience";
    public final static String KEY_PET_PHRASE = "DesktopPet_key_pet_phrase";
    public final static String KEY_PET_EMOTION = "DesktopPet_key_pet_emotion";


    public final static String KEY_THEME = "DesktopPet_key_theme";
    public final static String KEY_RING = "DesktopPet_key_ring";
    public final static String KEY_VIBRATE = "DesktopPet_key_vibrate";

    public final static String KEY_SERVICE_ACTION = "DesktopPet_service_action";
    public final static int SERVICE_START = 0;
    public final static int SERVICE_RENAME = 1;
    public final static String KEY_NAME = "DesktopPet_name";

    public final static String KEY_RECEIVER_MAIN = "DesktopPet_receiver_main";

    public final static int[] ICONS = {R.mipmap.cat, R.mipmap.dog, R.mipmap.bird, R.mipmap.bear,
            R.mipmap.penguin, R.mipmap.tiger, R.mipmap.lion, R.mipmap.rabbit, R.mipmap.rabbit};

    public static final UUID BLUETOOTH_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    public static final String BLUETOOTH_NAME = "DesktopPet_Bluetooth";

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";


}
