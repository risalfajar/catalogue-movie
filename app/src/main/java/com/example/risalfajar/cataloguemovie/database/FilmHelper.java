package com.example.risalfajar.cataloguemovie.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.risalfajar.cataloguemovie.entity.FilmItems;

import java.util.ArrayList;

import static com.example.risalfajar.cataloguemovie.database.DatabaseContract.FilmColumns.DATE;
import static com.example.risalfajar.cataloguemovie.database.DatabaseContract.FilmColumns.DESCRIPTION;
import static com.example.risalfajar.cataloguemovie.database.DatabaseContract.FilmColumns.IMAGEURL;
import static com.example.risalfajar.cataloguemovie.database.DatabaseContract.FilmColumns.TITLE;
import static com.example.risalfajar.cataloguemovie.database.DatabaseContract.FilmColumns._ID;
import static com.example.risalfajar.cataloguemovie.database.DatabaseContract.TABLE_FILM_FAVORITE;

/**
 * Created by Risal Fajar on 6/1/2018.
 */

public class FilmHelper {
    private static String DATABASE_TABLE = TABLE_FILM_FAVORITE;
    private Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public FilmHelper(Context context){
        this.context = context;
    }

    public FilmHelper open() throws SQLException{
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        databaseHelper.close();
    }

    public ArrayList<FilmItems> query(){
        ArrayList<FilmItems> arrayList = new ArrayList<FilmItems>();
        Cursor cursor = database.query(DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                DatabaseContract.FilmColumns._ID + " DESC",
                null);
        cursor.moveToFirst();
        FilmItems film;
        if(cursor.getCount() > 0){
            do{
                film = new FilmItems(cursor);
                arrayList.add(film);
                cursor.moveToNext();
            }while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }
    public long insert(FilmItems film){
        ContentValues initialValues =  new ContentValues();
        initialValues.put(TITLE, film.getTitle());
        initialValues.put(DESCRIPTION, film.getLongDescription());
        initialValues.put(DATE, film.getDate());
        initialValues.put(IMAGEURL, film.getImagePath());
        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    public int update(FilmItems film){
        ContentValues args = new ContentValues();
        args.put(TITLE, film.getTitle());
        args.put(DESCRIPTION, film.getLongDescription());
        args.put(DATE, film.getDate());
        args.put(IMAGEURL, film.getImagePath());
        return database.update(DATABASE_TABLE, args, TITLE + "= '" + film.getTitle() + "'", null);
    }

    public int delete(int id){
        return database.delete(DATABASE_TABLE, _ID + " = '"+id+"'", null);
    }

    public Cursor queryByIdProvider(String id){
        return database.query(DATABASE_TABLE,null
                ,_ID + " = ?"
                ,new String[]{id}
                ,null
                ,null
                ,null
                ,null);
    }
    public Cursor queryProvider(){
        return database.query(DATABASE_TABLE
                ,null
                ,null
                ,null
                ,null
                ,null
                ,_ID + " DESC");
    }
    public long insertProvider(ContentValues values){
        return database.insert(DATABASE_TABLE,null,values);
    }
    public int updateProvider(String id,ContentValues values){
        return database.update(DATABASE_TABLE,values,_ID +" = ?",new String[]{id} );
    }
    public int deleteProvider(String id){
        return database.delete(DATABASE_TABLE,_ID + " = ?", new String[]{id});
    }

}
