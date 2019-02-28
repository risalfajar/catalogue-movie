package com.example.risalfajar.cataloguemovie;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Risal Fajar on 6/12/2018.
 */

public class UserPreferences {
    private static String KEY_NOTIFICATION_DAILY = "daily notification";
    private static String KEY_NOTIFICATION_RELEASE_TODAY = "release today notification";

    private SharedPreferences sharedPreferences;

    UserPreferences(Context context){
        String PREFS_NAME = "UserPref";
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setNotificationDaily(boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_NOTIFICATION_DAILY, value);
        editor.apply();
    }

    public boolean getNotificationDaily(){
        return sharedPreferences.getBoolean(KEY_NOTIFICATION_DAILY, false);
    }

    public boolean getNotificationReleaseToday(){
        return sharedPreferences.getBoolean(KEY_NOTIFICATION_RELEASE_TODAY, false);
    }

    public void setNotificationReleaseToday(boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_NOTIFICATION_RELEASE_TODAY, value);
        editor.apply();
    }
}
