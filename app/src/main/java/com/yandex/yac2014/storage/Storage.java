package com.yandex.yac2014.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.yandex.yac2014.YaCApplication;
import com.yandex.yac2014.model.Photo;

import java.util.List;

import nl.qbusict.cupboard.DatabaseCompartment;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by 7times6 on 17.10.14.
 */
public class Storage {

    // Simple synchronous methods

    public Long savePhoto(Photo photo) {
        return dataCompartment.put(photo);
    }

    public boolean deletePhoto(Photo photo) {
        return dataCompartment.delete(photo);
    }


    // Observable-based method

    public Observable<Photo> toggleLiked(final Photo photo) {
        return bindIoToMain(new Func0<Observable<Photo>>() {
            @Override
            public Observable<Photo> call() {
                if (photo.liked) {
                    photo.liked = false;
                    deletePhoto(photo);
                } else {
                    photo.liked = true;
                    savePhoto(photo);
                }
                return Observable.just(photo);
            }
        });
    }

    public Observable<List<Photo>> photos() {
        return bindIoToMain(new Func0<Observable<List<Photo>>>() {
            @Override
            public Observable<List<Photo>> call() {
                return Observable.just(dataCompartment.query(Photo.class).list());
            }
        });
    }

    // stuff

    private static <T> Observable<T> bindIoToMain(Func0<Observable<T>> func) {
        return Observable
                .defer(func)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    PhotoSqliteOpenHelper openHelper;
    SQLiteDatabase database;
    DatabaseCompartment dataCompartment;

    private static Storage instance;
    private static final Object INIT_LOCK = new Object();

    private Storage(Context context) {
        openHelper = new PhotoSqliteOpenHelper(context);
        database = openHelper.getWritableDatabase();
        dataCompartment = cupboard().withDatabase(database);
    }

    public static Storage get() {
        if (instance == null) {
            synchronized (INIT_LOCK) {
                if (instance == null) {
                    instance = new Storage(YaCApplication.instance);
                }
            }
        }
        return instance;
    }
}
