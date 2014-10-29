package com.yandex.yac2014.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yandex.yac2014.model.Photo;
import com.yandex.yac2014.model.User;
import com.yandex.yac2014.storage.convert.GenericFieldConverterFactory;
import com.yandex.yac2014.storage.convert.JsonConverter;

import nl.qbusict.cupboard.Cupboard;
import nl.qbusict.cupboard.CupboardBuilder;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;
import static nl.qbusict.cupboard.CupboardFactory.setCupboard;

/**
 * Created by 7times6 on 17.10.14.
 */
public class PhotoSqliteOpenHelper extends SQLiteOpenHelper {


    private final static String DB_FILE = "data.db";
    private final static int DB_VERSION = 11;

    static {
        setCupboard(
                new CupboardBuilder()
                .registerFieldConverter(User.class, new JsonConverter<User>(User.class))
                .registerFieldConverterFactory(new GenericFieldConverterFactory())
                .build()
        );

        cupboard().register(Photo.class);
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
