package com.example.risalfajar.cataloguemovie;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.risalfajar.cataloguemovie.entity.FilmItems;
import com.example.risalfajar.cataloguemovie.fragment.CardviewFilmFragment;
import com.example.risalfajar.cataloguemovie.fragment.SearchFragment;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    @BindView(R.id.container) ViewPager mViewPager;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabs) TabLayout tabLayout;

    private CardviewFilmFragment nowPlayingFragment = null;
    private CardviewFilmFragment upcomingFragment = null;
    private SearchFragment searchFragment = null;
    private UserPreferences userPreferences;
    private NotificationPublisher notificationPublisher;
    private List<FilmItems> releasedToday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        userPreferences = new UserPreferences(this);
        notificationPublisher = new NotificationPublisher(this);

        if(!userPreferences.getNotificationDaily()){
            Log.d(getClass().getSimpleName(), "Calling daily notification");
            NotificationCompat.Builder dailyNotificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.notif_daily_title))
                    .setContentText(getString(R.string.notif_daily_content))
                    .setSmallIcon(R.drawable.baseline_notification_important_white_48dp)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification_bell));
            Notification dailyNotification = dailyNotificationBuilder.build();

            notificationPublisher.scheduleDailyNotification(dailyNotification);
            userPreferences.setNotificationDaily(true);
        }
        if(!userPreferences.getNotificationReleaseToday()){
            Log.d(getClass().getSimpleName(), "Calling release today notification");
            notificationPublisher.scheduleReleaseTodayNotification();
            userPreferences.setNotificationReleaseToday(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_change_language) {
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
        }else if(id == R.id.menu_favorite_film){
            Intent intent = new Intent(MainActivity.this, FavoriteFilmActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    if(nowPlayingFragment == null){
                        nowPlayingFragment = new CardviewFilmFragment();
                        nowPlayingFragment.setUrl(FilmAsyncTaskLoader.URL_NOW_PLAYING);
                    }
                    return nowPlayingFragment;
                case 1:
                    if(upcomingFragment == null){
                        upcomingFragment = new CardviewFilmFragment();
                        upcomingFragment.setUrl(FilmAsyncTaskLoader.URL_UPCOMING);
                    }
                    return upcomingFragment;
                case 2:
                    if(searchFragment == null){
                        searchFragment = new SearchFragment();
                    }
                    return searchFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
