package com.yandex.yac2014;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.squareup.okhttp.OkHttpClient;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

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

        // Configure Timber
        Timber.plant(new Timber.DebugTree());

        // Use okhttp as model loader
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
        Glide.get(this)
            .register(GlideUrl.class,
                      InputStream.class,
                      new OkHttpUrlLoader.Factory(okHttpClient));
    }
}
