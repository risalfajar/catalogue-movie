package com.example.risalfajar.cataloguemovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.risalfajar.cataloguemovie.FilmDetailActivity;
import com.example.risalfajar.cataloguemovie.R;
import com.example.risalfajar.cataloguemovie.entity.FilmItems;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Risal Fajar on 5/24/2018.
 */

public class CardviewFilmAdapter extends RecyclerView.Adapter<CardviewFilmAdapter.CategoryViewHolder>{

    private Context context;
    public ArrayList<FilmItems> listFilms;

    public CardviewFilmAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<FilmItems> getListFilms() {
        return listFilms;
    }

    public void setListFilms(ArrayList<FilmItems> listFilms) {
        this.listFilms = listFilms;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_cardview_item, parent, false);
        return new CategoryViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        final FilmItems film = getListFilms().get(position);
        if(film.getImagePath() != null) {
            Glide.with(context)
                    .load(film.getImagePath())
                    .crossFade()
                    .into(holder.ivImgPhoto);
        }
        holder.tvFilmTitle.setText(film.getTitle());
        holder.tvFilmDesc.setText(film.getShortDescription());
        holder.tvFilmDate.setText(film.getDate());

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent filmDetailIntent = new Intent(v.getContext(), FilmDetailActivity.class);
                filmDetailIntent.putExtra(FilmDetailActivity.EXTRA_FILM_ITEM, film);
                v.getContext().startActivity(filmDetailIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListFilms().size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_film_title) TextView tvFilmTitle;
        @BindView(R.id.tv_film_desc) TextView tvFilmDesc;
        @BindView(R.id.tv_film_date) TextView tvFilmDate;
        @BindView(R.id.iv_film_poster) ImageView ivImgPhoto;
        @BindView(R.id.btn_detail) Button btnDetail;
        @BindView(R.id.btn_share) Button btnShare;

        CategoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
