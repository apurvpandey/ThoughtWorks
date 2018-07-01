package com.thoughtworks.apurvpandey.thoughtworks;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;
    }
}
