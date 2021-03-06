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
    public final static String ACTION_LISTEN_SERVICE = "com.persist.desktoppet.service.WXListenService";
    public final static String ACTION_DISPLAY_SERVICE = "com.persist.desktoppet.service.DisplayService";
    public final static String ACTION_ALARM = "com.persist.desktop.receiver.alarm_trigger_action";
    public final static String ACTION_MAIN_ACTIVITY = "android.intent.action.MAIN";
    public final static String ACTION_FEED_ACTIVITY = "com.persist.desktoppet.view.activity.FeedActivity";

    public final static String KEY_LAST_TIME = "DesktopPet_key_last_time";
    //data key
    public final static String KEY_PET = "DesktopPet_key_pet";
    public final static String KEY_PET_NAME = "DesktopPet_key_pet_name";
    public final static String KEY_PET_AGE = "DesktopPet_key_pet_age";
    public final static String KEY_PET_TYPE = "DesktopPet_key_pet_type";
    public final static String KEY_PET_SEX = "DesktopPet_key_pet_sex";
    public final static String KEY_PET_LEVEL = "DesktopPet_key_pet_level";
    public final static String KEY_PET_EXPERIENCE = "DesktopPet_key_pet_experience";
    public final static String KEY_PET_PHRASE = "DesktopPet_key_pet_phrase";
    public final static String KEY_PET_EMOTION = "DesktopPet_key_pet_emotion";
    public final static String KEY_PET_ID = "DesktopPet_key_pet_id";
    public final static String KEY_PET_ALONE = "DesktopPet_key_pet_alone";
    public final static String KEY_PET_POWER = "DesktopPet_key_pet_power";

    public final static String KEY_INTIMATE_PET_NAME = "DesktopPet_key_intimate_pet_name";
    public final static String KEY_INTIMATE_PET_AGE = "DesktopPet_key_intimate_pet_age";
    public final static String KEY_INTIMATE_PET_TYPE = "DesktopPet_key_intimate_pet_type";
    public final static String KEY_INTIMATE_PET_SEX = "DesktopPet_key_intimate_pet_sex";
    public final static String KEY_INTIMATE_PET_LEVEL = "DesktopPet_key_intimate_pet_level";
    public final static String KEY_INTIMATE_PET_EXPERIENCE = "DesktopPet_key_intimate_pet_experience";
    public final static String KEY_INTIMATE_PET_PHRASE = "DesktopPet_key_intimate_pet_phrase";
    public final static String KEY_INTIMATE_PET_EMOTION = "DesktopPet_key_intimate_pet_emotion";
    public final static String KEY_INTIMATE_PET_ID = "DesktopPet_key_intimate_pet_id";
    public final static String KEY_INTIMATE_PET_ALONE = "DesktopPet_key_intimate_pet_alone";
    public final static String KEY_INTIMATE_PET_POWER = "DesktopPet_key_intimate_pet_power";

    public final static String KEY_PET_LAST_X = "DesktopPet_key_pet_last_x";
    public final static String KEY_PET_LAST_Y = "DesktopPet_key_pet_last_y";

    public final static String KEY_IS_FIRST = "DesktopPet_key_is_first";

    public final static String KEY_THEME = "DesktopPet_key_theme";
    public final static String KEY_RECEIVE = "DesktopPet_key_receive";
    public final static String KEY_RING = "DesktopPet_key_ring";
    public final static String KEY_CARE = "DesktopPet_key_care";

    public final static String KEY_SERVICE_ACTION = "DesktopPet_service_action";
    public final static int SERVICE_START = 0;
    public final static int SERVICE_RENAME = 1;
    public final static int SERVICE_ALARM = 2;
    public final static int SERVICE_MSG = 3;
    public final static int SERVICE_UPDATE = 4;//for wx listen
    public final static String KEY_NAME = "DesktopPet_name";
    public final static String KEY_MSG = "DesktopPet_msg";


    public final static String KEY_RECEIVER_MAIN = "DesktopPet_receiver_main";
    public final static String KEY_RECEIVER_FEED = "DesktopPet_receiver_feed";
    public final static String KEY_RECEIVER_MAIN_ACTION = "DesktopPet_receiver_main_action";
    public final static String KEY_RECEIVER_FEED_ACTION = "DesktopPet_receiver_feed_action";
    public final static String KEY_RECEIVER_MAIN_VALUE = "DesktopPet_receiver_main_value";
    public final static String KEY_RECEIVER_FEED_VALUE = "DesktopPet_receiver_feed_value";

    public final static int RECEIVER_ACTION_DESTROY = 0;
    public final static int RECEIVER_ACTION_UPDATE = 1;



    public final static int[] ICONS = {R.mipmap.cat, R.mipmap.dog, R.mipmap.bird, R.mipmap.bear,
            R.mipmap.penguin, R.mipmap.tiger, R.mipmap.lion, R.mipmap.rabbit, R.mipmap.rabbit};

    public final static int[] PET_ICONS = {R.mipmap.ic_pet_boy, R.mipmap.ic_pet_girl};

    public static final UUID BLUETOOTH_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    public static final String BLUETOOTH_NAME = "DesktopPet_Bluetooth";

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String DEVICE_ADDRESS = "device_address";
    public static final String TOAST = "toast";

    public static final int COLOR_WEAK = 0xffdc143c;
    public static final int COLOR_NORMAL = 0xffffff00;
    public static final int COLOR_HEALTHY = 0xff00ff7f;

    public static final int MOVIE_NORMAL = 0;
    public static final int MOVIE_HUNGRY = 1;
    public static final int MOVIE_ALARM = 2;
    public static final int MOVIE_MSG = 3;

    public static final int[] MOVIES = {R.raw.normal, R.raw.hungry, R.raw.alarm, R.raw.msg};

    public static final int[] MOVIES_GIRL = {R.raw.normal_girl, R.raw.hungry_girl, R.raw.alarm, R.raw.msg_girl};

    public static final long DECREASE_DURATION = 10*1000;
    public static final int DECREASE_POWER = 1;

    public static final int POWER_HUNGRY = 20;

}