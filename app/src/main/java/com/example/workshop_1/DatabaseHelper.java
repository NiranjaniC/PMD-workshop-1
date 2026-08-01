package com.example.workshop_1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database information
    public static final String DATABASE_NAME = "CountryDB";
    public static final int DATABASE_VERSION = 1;

    // Table information
    public static final String TABLE_NAME = "Country";

    // Column names
    public static final String COL_ID = "_id";
    public static final String COL_COUNTRY = "country";
    public static final String COL_CURRENCY = "currency";

    // SQL query to create the table
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_COUNTRY + " TEXT NOT NULL, " +
                    COL_CURRENCY + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);

    }
}