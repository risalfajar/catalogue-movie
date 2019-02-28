package com.example.risalfajar.cataloguemovie;

import android.content.ContentValues;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.risalfajar.cataloguemovie.database.DatabaseContract;
import com.example.risalfajar.cataloguemovie.entity.FilmItems;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.risalfajar.cataloguemovie.database.DatabaseContract.CONTENT_URI;
import static com.example.risalfajar.cataloguemovie.database.DatabaseContract.FilmColumns.DATE;
import static com.example.risalfajar.cataloguemovie.database.DatabaseContract.FilmColumns.DESCRIPTION;
import static com.example.risalfajar.cataloguemovie.database.DatabaseContract.FilmColumns.IMAGEURL;
import static com.example.risalfajar.cataloguemovie.database.DatabaseContract.FilmColumns.TITLE;
import static com.example.risalfajar.cataloguemovie.database.DatabaseContract.getColumnString;


public class FilmDetailActivity extends AppCompatActivity {

    //    public static final String EXTRA_FILM_DATA_POSITION = "film data position";
    public static final String EXTRA_FILM_ITEM = "film item";
    public static final String EXTRA_MODE = "mode";
    public static final String MODE_URI = "URI";
    public static final String MODE_EXTRA = "EXTRA";

    @BindView(R.id.tv_film_date) TextView tvFilmDate;
    @BindView(R.id.tv_film_desc) TextView tvFilmDesc;
    @BindView(R.id.iv_film_poster) ImageView ivFilmPoster;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.fab) FloatingActionButton fab;

    private String filmTitle, filmDesc, filmDate, filmImgPath;
    private String mode;
    private FilmItems film;
    private Uri uri;
    private boolean isFaved;

    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);

        ButterKnife.bind(this);

        if(getIntent().getStringExtra(EXTRA_MODE) != null)
            mode = getIntent().getStringExtra(EXTRA_MODE);
        else
            mode = MODE_EXTRA;

        if(mode.equals(MODE_URI)){
            final Uri uri = getIntent().getData();
            if (uri != null) {
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    if (cursor.moveToFirst())
                        film = new FilmItems(cursor);
                    cursor.close();
                }
            }
        }else{
            film = getIntent().getParcelableExtra(EXTRA_FILM_ITEM);
        }

        filmTitle = film.getTitle();
        filmDesc = film.getLongDescription();
        filmDate = film.getDate();
        filmImgPath = film.getImagePath();

        collapsingToolbarLayout.setTitle(filmTitle);
        tvFilmDesc.setText(filmDesc);
        tvFilmDate.setText(filmDate);

        if (filmImgPath != null) {
            Glide.with(this)
                    .load(filmImgPath)
                    .crossFade()
                    .into(ivFilmPoster);
        }

        refreshCursor();
        refreshFab();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFaved) {
                    if (uri != null) {
                        getContentResolver().delete(uri, null, null);
                        isFaved = !isFaved;
                    }
                } else {
                    ContentValues values = new ContentValues();
                    values.put(TITLE, filmTitle);
                    values.put(DESCRIPTION, filmDesc);
                    values.put(DATE, filmDate);
                    values.put(IMAGEURL, filmImgPath);
                    getContentResolver().insert(CONTENT_URI, values);
                    isFaved = !isFaved;
                    refreshCursor();
                }
                refreshFab();
            }
        });
    }

    private void refreshCursor(){
        Cursor cursor = getContentResolver().query(CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    if(getColumnString(cursor, TITLE).equals(filmTitle)){
                        isFaved = true;
                        uri = Uri.parse(CONTENT_URI + "/" + getColumnString(cursor, DatabaseContract.FilmColumns._ID));
                        break;
                    }
                    isFaved = false;
                    cursor.moveToNext();
                }while (!cursor.isAfterLast());
            } else {
                isFaved = false;
            }
            cursor.close();
        } else {
            isFaved = false;
        }
    }

    private void refreshFab() {
        if (isFaved) {
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gold)));
            fab.setRippleColor(getResources().getColor(R.color.colorAccent));
        } else {
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
            fab.setRippleColor(getResources().getColor(R.color.gold));
        }
    }
}
