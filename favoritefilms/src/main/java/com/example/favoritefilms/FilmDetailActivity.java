package com.example.favoritefilms;

import android.content.ContentValues;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.favoritefilms.entity.FilmItems;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.favoritefilms.database.DatabaseContract.CONTENT_URI;
import static com.example.favoritefilms.database.DatabaseContract.FilmColumns.IMAGEURL;
import static com.example.favoritefilms.database.DatabaseContract.FilmColumns.TITLE;
import static com.example.favoritefilms.database.DatabaseContract.FilmColumns.DESCRIPTION;
import static com.example.favoritefilms.database.DatabaseContract.FilmColumns.DATE;


public class FilmDetailActivity extends AppCompatActivity {

    private String filmTitle, filmDesc, filmDate, filmImgPath;
    private FilmItems film;
    private boolean isFaved;

    @BindView(R.id.tv_film_date) TextView tvFilmDate;
    @BindView(R.id.tv_film_desc) TextView tvFilmDesc;
    @BindView(R.id.iv_film_poster) ImageView ivFilmPoster;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.fab) FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);

        ButterKnife.bind(this);

        final Uri uri = getIntent().getData();
        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst())
                    film = new FilmItems(cursor);
                cursor.close();
            }
        }

        filmTitle = film.getTitle();
        filmDesc = film.getLongDescription();
        filmDate = film.getDate();
        filmImgPath = film.getImagePath();
        isFaved = true;

        refreshFab();
        collapsingToolbarLayout.setTitle(filmTitle);
        tvFilmDesc.setText(filmDesc);
        tvFilmDate.setText(filmDate);

        if (filmImgPath != null) {
            Glide.with(this)
                    .load(filmImgPath)
                    .crossFade()
                    .into(ivFilmPoster);
        }
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
                }
                refreshFab();
            }
        });
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
