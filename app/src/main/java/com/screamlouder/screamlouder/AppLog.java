package com.screamlouder.screamlouder;

import android.util.Log;
/**
 * Created by Oyvind on 29.09.2016.
 */

public class AppLog {

    private static final String  APP_TAG = "AudioRecorder";

    public static int logString(String msg){
        return Log.i(APP_TAG, msg);
    }

}
