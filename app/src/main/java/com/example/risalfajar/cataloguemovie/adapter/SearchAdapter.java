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
 * Created by Risal Fajar on 4/26/2018.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private static ArrayList<FilmItems> mData = new ArrayList<>();
    private Context context;

    public SearchAdapter(Context context) {
        this.context = context;
    }

    public void setmData(ArrayList<FilmItems> items){
        mData = items;
    }

    public void addItem(final FilmItems item){
        mData.add(item);
    }

    public void clearmData(){
        mData.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_cardview_item, parent, false);
        return new SearchAdapter.ViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final FilmItems film = mData.get(position);
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
    public long getItemId(int position) {
        return mData.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_film_title) TextView tvFilmTitle;
        @BindView(R.id.tv_film_desc) TextView tvFilmDesc;
        @BindView(R.id.tv_film_date) TextView tvFilmDate;
        @BindView(R.id.iv_film_poster) ImageView ivImgPhoto;
        @BindView(R.id.btn_detail) Button btnDetail;
        @BindView(R.id.btn_share) Button btnShare;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static FilmItems getData(int position){
        return mData.get(position);
    }
}
