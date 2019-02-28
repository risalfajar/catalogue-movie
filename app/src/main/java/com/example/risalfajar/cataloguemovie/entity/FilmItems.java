package com.example.risalfajar.cataloguemovie.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.risalfajar.cataloguemovie.database.DatabaseContract.FilmColumns.DATE;
import static com.example.risalfajar.cataloguemovie.database.DatabaseContract.FilmColumns.DESCRIPTION;
import static com.example.risalfajar.cataloguemovie.database.DatabaseContract.FilmColumns.IMAGEURL;
import static com.example.risalfajar.cataloguemovie.database.DatabaseContract.FilmColumns.TITLE;
import static com.example.risalfajar.cataloguemovie.database.DatabaseContract.FilmColumns._ID;
import static com.example.risalfajar.cataloguemovie.database.DatabaseContract.getColumnInt;
import static com.example.risalfajar.cataloguemovie.database.DatabaseContract.getColumnString;

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
    public static final String imgServer ="http://image.tmdb.org/t/p/";

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
                imagePath = imgServer + imageSize + object.getString("poster_path");
            }
            else {
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
        this.id = getColumnInt(cursor, _ID);
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

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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
        dest.writeString(this.shortDescription);
        dest.writeString(this.date);
        dest.writeString(this.imagePath);
    }

    protected FilmItems(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.longDescription = in.readString();
        this.shortDescription = in.readString();
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

    public static class ImagePath{
        public static String changeImgSize(String url, String newImgSize){
            String fileNameUrl = "";
                if('/' == url.charAt(imgServer.length() + 3)){ //w92
                    fileNameUrl = url.substring(imgServer.length() + 3);
                }
                else if('/' == url.charAt(imgServer.length() + 4)){ //w154 - w780
                    fileNameUrl = url.substring(imgServer.length() + 4);
                }
                else if('/' == url.charAt(imgServer.length() + 8)){ //original
                    fileNameUrl = url.substring(imgServer.length() + 8);
                }
                return imgServer + newImgSize + fileNameUrl;
        }
    }
}
