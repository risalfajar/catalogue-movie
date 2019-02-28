package com.example.favoritefilms;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.favoritefilms.adapter.FavoriteFilmsAdapter;
import com.example.favoritefilms.database.DatabaseContract;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.favoritefilms.database.DatabaseContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private FavoriteFilmsAdapter favoriteFilmsAdapter;

    @BindView(R.id.lv_films) ListView lvFavFilms;


    private final int LOAD_FAV_FILMS_ID = 100;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        favoriteFilmsAdapter = new FavoriteFilmsAdapter(this, null, true);
        lvFavFilms.setAdapter(favoriteFilmsAdapter);
        getSupportLoaderManager().initLoader(LOAD_FAV_FILMS_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().initLoader(LOAD_FAV_FILMS_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        favoriteFilmsAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        favoriteFilmsAdapter.swapCursor(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(LOAD_FAV_FILMS_ID);
    }
}
