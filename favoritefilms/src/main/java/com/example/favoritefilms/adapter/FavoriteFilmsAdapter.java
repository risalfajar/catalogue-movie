package com.example.favoritefilms.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.favoritefilms.FilmDetailActivity;
import com.example.favoritefilms.MainActivity;
import com.example.favoritefilms.R;
import com.example.favoritefilms.database.DatabaseContract;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.favoritefilms.database.DatabaseContract.CONTENT_URI;
import static com.example.favoritefilms.database.DatabaseContract.FilmColumns.DATE;
import static com.example.favoritefilms.database.DatabaseContract.FilmColumns.DESCRIPTION;
import static com.example.favoritefilms.database.DatabaseContract.FilmColumns.IMAGEURL;
import static com.example.favoritefilms.database.DatabaseContract.FilmColumns.TITLE;
import static com.example.favoritefilms.database.DatabaseContract.getColumnString;

/**
 * Created by Risal Fajar on 6/5/2018.
 */

public class FavoriteFilmsAdapter extends CursorAdapter {

    @BindView(R.id.tv_film_title) TextView tvFilmTitle;
    @BindView(R.id.tv_film_desc) TextView tvFilmDesc;
    @BindView(R.id.tv_film_date) TextView tvFilmDate;
    @BindView(R.id.iv_film_poster) ImageView ivPoster;
    @BindView(R.id.btn_detail) Button btnDetail;

    public FavoriteFilmsAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.film_cardview_item, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        if(cursor != null){
            ButterKnife.bind(this, view);

            tvFilmTitle.setText(getColumnString(cursor, TITLE));
            tvFilmDesc.setText(getColumnString(cursor, DESCRIPTION));
            tvFilmDate.setText(getColumnString(cursor, DATE));
            String imageUrl = getColumnString(cursor, IMAGEURL);

            Glide.with(context)
                    .load(imageUrl)
                    .crossFade()
                    .into(ivPoster);

            final String selectedId = getColumnString(cursor, DatabaseContract.FilmColumns._ID);

            btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FilmDetailActivity.class);
                    Uri uri = Uri.parse(CONTENT_URI + "/" + selectedId);
                    intent.setData(uri);
                    context.startActivity(intent);
                }
            });
        }
    }
}
