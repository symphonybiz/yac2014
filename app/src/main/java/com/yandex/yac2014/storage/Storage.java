package com.yandex.yac2014.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.yandex.yac2014.YaCApplication;
import com.yandex.yac2014.model.Photo;

import java.util.List;

import nl.qbusict.cupboard.DatabaseCompartment;
import rx.Observable;
import rx.functions.Func0;

import static nl.qbusict.cupboard.CupboardFactory.*;

/**
 * Created by 7times6 on 17.10.14.
 */
public class Storage {

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

    public Long savePhoto(Photo photo) {
        return dataCompartment.put(photo);
    }

    public boolean deletePhoto(Photo photo) {
        return dataCompartment.delete(photo);
    }

    public Observable<List<Photo>> photos() {
        return Observable.defer(new Func0<Observable<List<Photo>>>() {
            @Override
            public Observable<List<Photo>> call() {
                return Observable.just(dataCompartment.query(Photo.class).list());
            }
        });
    }
}
