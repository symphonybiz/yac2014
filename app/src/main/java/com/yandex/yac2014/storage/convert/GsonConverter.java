package com.yandex.yac2014.storage.convert;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;

import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.FieldConverter;
import timber.log.Timber;

/**
 * Created by 7times6 on 17.10.14.
 */
public class GsonConverter<T> implements FieldConverter<T> {

    final Class<T> clazz;
    final Gson gson;

    public GsonConverter(Class<T> clazz) {
        this.clazz = clazz;
        this.gson  = new Gson();
    }

    @Override
    public EntityConverter.ColumnType getColumnType() {
        return EntityConverter.ColumnType.TEXT;
    }

    @Override
    public T fromCursorValue(Cursor cursor, int columnIndex) {
        String jsonString = cursor.getString(columnIndex);
        T result = gson.fromJson(jsonString, clazz);
        return result;
    }

    @Override
    public void toContentValue(T value, String key, ContentValues values) {
        final String jsonString = gson.toJson(value, clazz);
        values.put(key, jsonString);
    }
}
