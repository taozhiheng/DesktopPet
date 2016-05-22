package com.persist.desktoppet.util;

import android.util.Log;

/**
 * Created by taozhiheng on 16-2-20.
 */
public class LogUtil{

    private static boolean showLog = true;

    private static void showLog(boolean show)
    {
        showLog = show;
    }

    public static int v(String tag, String msg)
    {
        if(showLog)
            return Log.d(tag, msg);
        return 0;
    }

    public static int v(String tag, String msg, Throwable tr)
    {
        if(showLog)
            return Log.v(tag, msg, tr);
        return 0;
    }

    public static int d(String tag, String msg)
    {
        if(showLog)
            return Log.d(tag, msg);
        return 0;
    }

    public static int d(String tag, String msg, Throwable tr)
    {
        if(showLog)
            return Log.d(tag, msg, tr);
        return 0;
    }

    public static int i(String tag, String msg)
    {
        if(showLog)
            return Log.i(tag, msg);
        return 0;
    }

    public static int i(String tag, String msg, Throwable tr)
    {
        if(showLog)
            return Log.i(tag, msg, tr);
        return 0;
    }

    public static int w(String tag, String msg)
    {
        if(showLog)
            return Log.w(tag, msg);
        return 0;
    }

    public static int w(String tag, String msg, Throwable tr)
    {
        if(showLog)
            return Log.w(tag, msg, tr);
        return 0;
    }

    public static int e(String tag, String msg)
    {
        if(showLog)
            return Log.e(tag, msg);
        return 0;
    }

    public static int e(String tag, String msg, Throwable tr)
    {
        if(showLog)
            return Log.e(tag, msg, tr);
        return 0;
    }


}
