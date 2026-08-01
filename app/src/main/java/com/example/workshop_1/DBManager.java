package com.example.workshop_1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DBManager(Context context) {
        this.context = context;
    }

    // Open Database
    public DBManager open() {

        dbHelper = new DatabaseHelper(context);

        database = dbHelper.getWritableDatabase();

        return this;
    }

    // Close Database
    public void close() {

        dbHelper.close();

    }

    // Insert Data
    public void insert(String country, String currency) {

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COL_COUNTRY, country);

        values.put(DatabaseHelper.COL_CURRENCY, currency);

        database.insert(DatabaseHelper.TABLE_NAME, null, values);

    }

    // Read Data
    public Cursor fetch() {

        String[] columns = {
                DatabaseHelper.COL_ID,
                DatabaseHelper.COL_COUNTRY,
                DatabaseHelper.COL_CURRENCY
        };

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    // Update Data
    public int update(long id, String country, String currency) {

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COL_COUNTRY, country);

        values.put(DatabaseHelper.COL_CURRENCY, currency);

        return database.update(
                DatabaseHelper.TABLE_NAME,
                values,
                DatabaseHelper.COL_ID + "=?",
                new String[]{String.valueOf(id)}
        );
    }

    // Delete Data
    public void delete(long id) {

        database.delete(
                DatabaseHelper.TABLE_NAME,
                DatabaseHelper.COL_ID + "=?",
                new String[]{String.valueOf(id)}
        );

    }

}