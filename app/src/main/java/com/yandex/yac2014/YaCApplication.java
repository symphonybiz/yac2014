package com.yandex.yac2014;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by 7times6 on 17.10.14.
 */
public class YaCApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
