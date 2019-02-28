package com.example.risalfajar.cataloguemovie.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.risalfajar.cataloguemovie.FilmAsyncTaskLoader;
import com.example.risalfajar.cataloguemovie.R;
import com.example.risalfajar.cataloguemovie.adapter.CardviewFilmAdapter;
import com.example.risalfajar.cataloguemovie.entity.FilmItems;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class CardviewFilmFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<FilmItems>> {

    @BindView(R.id.rv_film_list) RecyclerView rvFilmItems;

    private CardviewFilmAdapter listFilmAdapter;
    private String url;
    private FilmAsyncTaskLoader asyncTaskLoader;

    public CardviewFilmFragment() {
        // Required empty public constructor
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cardview, container, false);
        ButterKnife.bind(this, view);

        rvFilmItems.setHasFixedSize(true);

        getLoaderManager().restartLoader(0, null, CardviewFilmFragment.this);

        return view;
    }

    @Override
    public Loader<ArrayList<FilmItems>> onCreateLoader(int id, Bundle args) {
        asyncTaskLoader = new FilmAsyncTaskLoader(getContext(), url);
        return asyncTaskLoader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<FilmItems>> loader, ArrayList<FilmItems> data) {
        rvFilmItems.setLayoutManager(new LinearLayoutManager(getContext()));
        listFilmAdapter = new CardviewFilmAdapter(getContext());
        listFilmAdapter.setListFilms(data);
        rvFilmItems.setAdapter(listFilmAdapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<FilmItems>> loader) {
        listFilmAdapter.setListFilms(null);
    }

    @Override
    public void onDestroy() {
        asyncTaskLoader.cancelLoad();
        getLoaderManager().destroyLoader(0);
        super.onDestroy();
    }
}
