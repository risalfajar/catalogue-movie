package com.example.risalfajar.cataloguemovie;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.risalfajar.cataloguemovie.entity.FilmItems;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Risal Fajar on 6/12/2018.
 */

public class NotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification id";
    public static String NOTIFICATION = "notification";
    public static int NOTIFICATION_DAILY_REMINDER = 1;
    public static int NOTIFICATION_NOW_RELEASE = 2;
    private List<FilmItems> releasedToday;
    private Context context;

    public NotificationPublisher(Context context) {
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        this.context = context;

        Log.d(getClass().getSimpleName(), "NOTIF ID: " + id);

        if(id == NOTIFICATION_DAILY_REMINDER){
            Notification notification = intent.getParcelableExtra(NOTIFICATION);
            notificationManager.notify(id, notification);
        }else if(id == NOTIFICATION_NOW_RELEASE){
            releasedToday = new ArrayList<>();
            try {
                releasedToday = new AsyncTaskData().execute(FilmAsyncTaskLoader.URL_NOW_PLAYING).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            int notifId = id;
            releasedToday = getReleasedToday(releasedToday);
            for(FilmItems item:releasedToday){
                Notification.Builder notificationBuilder = new Notification.Builder(context)
                        .setContentTitle(item.getTitle())
                        .setContentText(item.getTitle() + context.getString(R.string.notif_is_released))
                        .setSmallIcon(R.drawable.baseline_notification_important_white_48dp)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notification_bell));

                notifId++;
                notificationManager.notify(notifId, notificationBuilder.build());
            }
        }
    }

    public void scheduleDailyNotification(Notification notification){
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, NotificationPublisher.NOTIFICATION_DAILY_REMINDER);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar alarmStartTime = Calendar.getInstance();
        alarmStartTime.set(Calendar.HOUR_OF_DAY, 7);
        alarmStartTime.set(Calendar.MINUTE, 00);
        alarmStartTime.set(Calendar.SECOND, 0);

        if(new Date().after(alarmStartTime.getTime())){
            alarmStartTime.set(Calendar.DATE, alarmStartTime.get(Calendar.DATE) + 1);
        }

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, alarmStartTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void scheduleReleaseTodayNotification(){
        Log.d(getClass().getSimpleName(), "Schedule release today..");
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, NotificationPublisher.NOTIFICATION_NOW_RELEASE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 101, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar alarmStartTime = Calendar.getInstance();
        alarmStartTime.setTime(new Date());
        alarmStartTime.set(Calendar.HOUR_OF_DAY, 8);
        alarmStartTime.set(Calendar.MINUTE, 00);
        alarmStartTime.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, alarmStartTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private List<FilmItems> getReleasedToday(List<FilmItems> items){
        List<FilmItems> today = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calNow = Calendar.getInstance();
        Calendar calFilm = Calendar.getInstance();

        calNow.setTime(new Date());

        for(FilmItems item:items){
            Date filmDate = null;
            try {
                filmDate = dateFormat.parse(item.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(filmDate != null){
                calFilm.setTime(filmDate);
                boolean isReleasedToday = (calNow.get(Calendar.YEAR) == calFilm.get(Calendar.YEAR)) &&
                        (calNow.get(Calendar.MONTH) == calFilm.get(Calendar.MONTH)) &&
                        (calNow.get(Calendar.DAY_OF_MONTH) == calFilm.get(Calendar.DAY_OF_MONTH));

                if(isReleasedToday)
                    today.add(item);
            }else
                calFilm.clear();

            Log.d(getClass().getSimpleName(), "Date now: " + calNow.get(Calendar.YEAR) + "/" + calNow.get(Calendar.MONTH) + "/" + calNow.get(Calendar.DAY_OF_MONTH) + "\n" +
                    "Date of film " + item.getTitle() + ": " + calFilm.get(Calendar.YEAR) + "/" + calFilm.get(Calendar.MONTH) + "/" + calFilm.get(Calendar.DAY_OF_MONTH) + "\n" +
                    "is this film released today? " + ((calNow.get(Calendar.YEAR) == calFilm.get(Calendar.YEAR)) && (calNow.get(Calendar.MONTH) == calFilm.get(Calendar.MONTH)) && (calNow.get(Calendar.DAY_OF_MONTH) == calFilm.get(Calendar.DAY_OF_MONTH))));
        }

        return today;
    }

    class AsyncTaskData extends AsyncTask<String, Void, List<FilmItems>> {
        @Override
        protected List<FilmItems> doInBackground(String... strings) {
            SyncHttpClient client = new SyncHttpClient();
            final ArrayList<FilmItems> filmItemses = new ArrayList<>();
            String url = strings[0];

            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                    setUseSynchronousMode(true);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String result = new String(responseBody);
                        JSONObject responseObject = new JSONObject(result);
                        JSONArray resultList = responseObject.getJSONArray("results");

                        if (result.length() > 0) {
                            for (int i = 0; i < resultList.length(); i++) {
                                JSONObject film = resultList.getJSONObject(i);
                                if (film != null) {
                                    FilmItems filmItems = new FilmItems(film);
                                    filmItemses.add(filmItems);
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
            return filmItemses;
        }
    }
}
