package com.example.risalfajar.cataloguemovie.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.risalfajar.cataloguemovie.database.DatabaseContract.TABLE_FILM_FAVORITE;

/**
 * Created by Risal Fajar on 6/1/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper{
    private static String DATABASE_NAME = "dbfilm";
    private static final int DATABASE_VERSION = 1;

    public static final String SQL_CREATE_TABLE_FAVORITE = String.format("CREATE TABLE %s" +
            " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL)",
            TABLE_FILM_FAVORITE,
            DatabaseContract.FilmColumns._ID,
            DatabaseContract.FilmColumns.TITLE,
            DatabaseContract.FilmColumns.DESCRIPTION,
            DatabaseContract.FilmColumns.DATE,
            DatabaseContract.FilmColumns.IMAGEURL
    );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILM_FAVORITE);
        onCreate(db);
    }
}
