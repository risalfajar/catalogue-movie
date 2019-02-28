package com.example.risalfajar.cataloguemovie.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Risal Fajar on 6/1/2018.
 */

public class DatabaseContract {
    public static String TABLE_FILM_FAVORITE = "filmfavorite";

    public static final class FilmColumns implements BaseColumns{
        public static String TITLE = "title";
        public static String DESCRIPTION = "description";
        public static String DATE = "date";
        public static String IMAGEURL = "imageurl";
    }

    public static final String AUTHORITY = "com.example.risalfajar.cataloguemovie";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_FILM_FAVORITE)
            .build();

    public static String getColumnString(Cursor cursor, String columnName){
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName){
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static long getColumnLong(Cursor cursor, String columnName){
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }
}
