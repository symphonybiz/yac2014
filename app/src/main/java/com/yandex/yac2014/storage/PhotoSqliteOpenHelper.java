package com.yandex.yac2014.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yandex.yac2014.model.Image;
import com.yandex.yac2014.model.Photo;
import com.yandex.yac2014.model.User;

import static nl.qbusict.cupboard.CupboardFactory.*;

/**
 * Created by 7times6 on 17.10.14.
 */
public class PhotoSqliteOpenHelper extends SQLiteOpenHelper {


    private final static String DB_FILE = "data.db";
    private final static int DB_VERSION = 1;

    static {
        cupboard().register(Photo.class);
        cupboard().register(User.class);
        cupboard().register(Image.class);
    }

    public PhotoSqliteOpenHelper(Context context) {
        super(context, DB_FILE, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        cupboard().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        cupboard().withDatabase(db).upgradeTables();
    }
}
