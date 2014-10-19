package com.yandex.yac2014.storage.convert;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.FieldConverter;
import timber.log.Timber;

/**
 * Created by 7times6 on 17.10.14.
 * <br>
 * Do not use it. Consider {@link com.yandex.yac2014.storage.convert.GsonConverter}
 */
@Deprecated
public class SerializableConverter<T extends Serializable> implements FieldConverter<T> {

    @Override
    public T fromCursorValue(Cursor cursor, int columnIndex) {
        byte[] binary = cursor.getBlob(columnIndex);

        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(binary));
            T res = (T) ois.readObject();
            return res;
        } catch (Exception e) {
            Timber.e(e, "Cannot read object.");
        }

        return null;
    }

    @Override
    public void toContentValue(T value, String key, ContentValues values) {

        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
            ObjectOutputStream ois = new ObjectOutputStream(byteArrayOutputStream);
            ois.writeObject(value);
            ois.close();

            byte[] binary = byteArrayOutputStream.toByteArray();
            values.put(key, binary);
        } catch (Exception e) {
            Timber.e(e, "Cannot write object.");
        }
    }

    @Override
    public EntityConverter.ColumnType getColumnType() {
        return EntityConverter.ColumnType.BLOB;
    }
}
