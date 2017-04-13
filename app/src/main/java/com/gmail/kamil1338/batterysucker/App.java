package com.gmail.kamil1338.batterysucker;

import android.app.Application;
import android.content.Context;

/**
 * Created by kamilpierudzki on 31/03/2017.
 */

public class App extends Application {

    private static App instance;

    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        instance.context = getApplicationContext();
    }

    public static Context getContext() {
        return instance.context;
    }
}
