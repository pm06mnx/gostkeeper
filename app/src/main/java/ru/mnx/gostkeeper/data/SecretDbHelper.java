package ru.mnx.gostkeeper.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import ru.mnx.gostkeeper.data.entity.Secret;
import ru.mnx.gostkeeper.data.entity.SecretWithData;

/**
 * Обертка для SQLite
 */
public class SecretDbHelper extends SQLiteOpenHelper {

    private static final int SCHEMA_VERSION = 1;
    private static final String DATABASE_FILE = "secrets.db";
    private static final String TABLE_SECRETS = "secrets";
    private static final String TABLE_SECRETS_FIELD_ID = BaseColumns._ID;
    private static final String TABLE_SECRETS_FIELD_NAME = "name";
    private static final String TABLE_SECRETS_FIELD_DATA = "data";

    public SecretDbHelper(Context context) {
        super(context, DATABASE_FILE, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_SECRETS + " (" +
                TABLE_SECRETS_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TABLE_SECRETS_FIELD_NAME + " BLOB," +
                TABLE_SECRETS_FIELD_DATA + " BLOB" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new IllegalStateException("Can't upgrade version "+oldVersion+" to "+newVersion);
    }

    /**
     * @return список секретов без защищаемых данных
     */
    public List<Secret> getSecretList() {
        List<Secret> result = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(
                    TABLE_SECRETS,
                    new String[]{TABLE_SECRETS_FIELD_ID, TABLE_SECRETS_FIELD_NAME},
                    null,
                    null,
                    null,
                    null,
                    BaseColumns._ID + " ASC"
            );
            while (cursor.moveToNext()) {
                result.add(new Secret(cursor.getInt(0), new String(cursor.getBlob(1))));
            }
        } finally {
            safeClose(cursor);
        }
        return result;
    }

    private void safeClose(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }


    /**
     * Создает секрет
     *
     * @param name наименование секрета
     * @param data защищаемая информация
     * @return идентификатор нового секрета или -1 в случае ошибки
     */
    public long createSecret(byte[] name, byte[] data) {
        ContentValues row = new ContentValues();
        row.put(TABLE_SECRETS_FIELD_NAME, name);
        row.put(TABLE_SECRETS_FIELD_DATA, data);
        return getWritableDatabase().insert(
                TABLE_SECRETS,
                null,
                row
        );
    }


    /**
     * @param id идентификатор секрета
     * @return вся информация о секрете, включая защищаемые данные
     */
    public SecretWithData getSecretWithData(int id) {
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(
                    TABLE_SECRETS,
                    new String[]{TABLE_SECRETS_FIELD_ID, TABLE_SECRETS_FIELD_NAME, TABLE_SECRETS_FIELD_DATA},
                    TABLE_SECRETS_FIELD_ID + " = " + id,
                    null,
                    null,
                    null,
                    null
            );
            if (cursor.moveToNext()) {
                return new SecretWithData(
                        cursor.getInt(0),
                        new String(cursor.getBlob(1)),
                        new String(cursor.getBlob(2))
                );
            }
        } finally {
            safeClose(cursor);
        }
        return null;
    }
}
