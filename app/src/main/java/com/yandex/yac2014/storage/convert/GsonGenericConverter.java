package com.yandex.yac2014.storage.convert;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.FieldConverter;

/**
 * Created by 7times6 on 17.10.14.
 */
public class GsonGenericConverter<T> implements FieldConverter<T> {

    final Type type;

    public GsonGenericConverter(Type type) {
        this.type = type;
    }

    @Override
    public T fromCursorValue(Cursor cursor, int columnIndex) {

        String jsonString = cursor.getString(columnIndex);
        Gson gson = new Gson();
        T result = gson.fromJson(jsonString, type);

        return result;
    }

    @Override
    public void toContentValue(T value, String key, ContentValues values) {
        Gson gson = new Gson();
        final String jsonString = gson.toJson(value, type);
        values.put(key, jsonString);
    }

    @Override
    public EntityConverter.ColumnType getColumnType() {
        return EntityConverter.ColumnType.TEXT;
    }
}
