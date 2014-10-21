package com.yandex.yac2014;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by 7times6 on 17.10.14.
 */
public class YaCApplication extends Application {

    public static YaCApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Timber.plant(new Timber.DebugTree());
    }
}
