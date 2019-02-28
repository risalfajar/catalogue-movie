package com.example.risalfajar.cataloguemovie;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.risalfajar.cataloguemovie.adapter.FavoriteFilmsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.risalfajar.cataloguemovie.database.DatabaseContract.CONTENT_URI;

public class FavoriteFilmActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private FavoriteFilmsAdapter favoriteFilmsAdapter;
    @BindView(R.id.lv_films) ListView lvFavFilms;

    private final int LOAD_FAV_FILMS_ID = 100;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_film);
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
