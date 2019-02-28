package com.example.favoritefilms.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.favoritefilms.database.DatabaseContract.FilmColumns.DATE;
import static com.example.favoritefilms.database.DatabaseContract.FilmColumns.DESCRIPTION;
import static com.example.favoritefilms.database.DatabaseContract.FilmColumns.IMAGEURL;
import static com.example.favoritefilms.database.DatabaseContract.FilmColumns.TITLE;
import static com.example.favoritefilms.database.DatabaseContract.getColumnInt;
import static com.example.favoritefilms.database.DatabaseContract.getColumnString;

/**
 * Created by Risal Fajar on 4/26/2018.
 */

public class FilmItems implements Parcelable {
    private int id;
    private String title, longDescription, shortDescription, date, imageSize = IMG_SIZE_WIDTH_342, imagePath;
    public static String IMG_SIZE_WIDTH_92 = "w92";
    public static String IMG_SIZE_WIDTH_154 = "w154";
    public static String IMG_SIZE_WIDTH_185 = "w185";
    public static String IMG_SIZE_WIDTH_342 = "w342";
    public static String IMG_SIZE_WIDTH_500 = "w500";
    public static String IMG_SIZE_WIDTH_780 = "w780";
    public static String IMG_SIZE_ORIGINAL = "original";
    private final String imgServer ="http://image.tmdb.org/t/p/";

    public FilmItems(JSONObject object){
        try{
            setId(object.getInt("id"));
            setTitle(object.getString("title"));
            if(object.getString("overview") != null){
                setLongDescription(object.getString("overview"));
                if(getLongDescription().length() > 100)
                    setShortDescription(getLongDescription().substring(0, 100) + "...");
                else
                    setShortDescription(getLongDescription());
            }
            if(object.getString("poster_path") != null) {
//                imagePathObject = new ImagePath(imgServer, imageSize, object.getString("poster_path"));
                imagePath = imgServer + imageSize + object.getString("poster_path");
            }
            else {
//                imagePathObject = null;
                imagePath = null;
            }
            if(object.getString("release_date") != null){
                DateFormat dateFormat = new SimpleDateFormat("E, MMM dd, yyyy");
                String dateString = object.getString("release_date");
                int year = Integer.parseInt(dateString.substring(0, 4));
                int month = Integer.parseInt(dateString.substring(5, 7));
                int day = Integer.parseInt(dateString.substring(8, 10));
                Date date = new Date(year-1900, month-1, day);
                setDate(dateFormat.format(date));
            }else
                date = null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public FilmItems() {

    }

    public FilmItems(Cursor cursor){
        this.id = getColumnInt(cursor, BaseColumns._ID);
        this.title = getColumnString(cursor, TITLE);
        this.longDescription = getColumnString(cursor, DESCRIPTION);
        this.date = getColumnString(cursor, DATE);
        this.imagePath = getColumnString(cursor, IMAGEURL);
    }


    public int getId() {
        return id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.longDescription);
        dest.writeString(this.date);
        dest.writeString(this.imagePath);
    }

    protected FilmItems(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.longDescription = in.readString();
        this.date = in.readString();
        this.imagePath = in.readString();
    }

    public static final Creator<FilmItems> CREATOR = new Creator<FilmItems>() {
        @Override
        public FilmItems createFromParcel(Parcel source) {
            return new FilmItems(source);
        }

        @Override
        public FilmItems[] newArray(int size) {
            return new FilmItems[size];
        }
    };
}
