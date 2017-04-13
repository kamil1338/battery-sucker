package com.gmail.kamil1338.batterysucker.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by kamilpierudzki on 03/04/2017.
 */

public class MainThread {

    private static Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    public static void runOnUiThread(Runnable runnable) {
        if (mainThreadHandler.getLooper() == Looper.myLooper()) {//if current thread is main thread
            runnable.run();
        } else {
            mainThreadHandler.post(runnable);
        }
    }
}
