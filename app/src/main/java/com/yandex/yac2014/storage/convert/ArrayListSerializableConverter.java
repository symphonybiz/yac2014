package com.yandex.yac2014.storage.convert;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.FieldConverter;
import timber.log.Timber;

/**
 * Created by 7times6 on 17.10.14.
 */
public class ArrayListSerializableConverter implements FieldConverter<ArrayList> {

    @Override
    public ArrayList fromCursorValue(Cursor cursor, int columnIndex) {
        byte[] binary = cursor.getBlob(columnIndex);

        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(binary));
            ArrayList arrayList = (ArrayList) ois.readObject();
            return arrayList;
        } catch (Exception e) {
            Timber.e(e, "Cannot read object.");
        }

        return null;
    }

    @Override
    public void toContentValue(ArrayList value, String key, ContentValues values) {

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
