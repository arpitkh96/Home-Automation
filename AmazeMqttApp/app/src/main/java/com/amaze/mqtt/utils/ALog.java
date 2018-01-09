package com.amaze.mqtt.utils;

import android.util.Log;

/**
 * Created by arpitkh996 on 07-09-2016.
 */

public class ALog {
    static boolean log=true;
    static String TAG="Amaze";
    public static void d(String tag,String text){
        if(log)
        Log.d(tag,text);
    }
    public static void d(String text){
        if(log)
            Log.d(TAG,text);
    }
}
