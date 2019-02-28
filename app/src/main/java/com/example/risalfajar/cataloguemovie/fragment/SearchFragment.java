package com.example.risalfajar.cataloguemovie.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.risalfajar.cataloguemovie.FilmAsyncTaskLoader;
import com.example.risalfajar.cataloguemovie.R;
import com.example.risalfajar.cataloguemovie.adapter.SearchAdapter;
import com.example.risalfajar.cataloguemovie.entity.FilmItems;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<FilmItems>> {

    @BindView(R.id.rv_film_list) RecyclerView rvSearchResult;
    @BindView(R.id.sv_search_film) SearchView searchView;

    private SearchAdapter adapter;
    private FilmAsyncTaskLoader asyncTaskLoader;

    static final String EXTRAS_MOVIE_TITLE = "extras_movie_title";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(getClass().getSimpleName(), "Searching...");

                Bundle bundle = new Bundle();
                bundle.putString(EXTRAS_MOVIE_TITLE, query.trim());
                getLoaderManager().restartLoader(0, bundle, SearchFragment.this);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }

    @Override
    public Loader<ArrayList<FilmItems>> onCreateLoader(int id, Bundle args) {
        String searchQuery = "";

        if(args != null)
            searchQuery = args.getString(EXTRAS_MOVIE_TITLE);
        asyncTaskLoader = new FilmAsyncTaskLoader(getContext(), FilmAsyncTaskLoader.URL_SEARCH, searchQuery);
        return asyncTaskLoader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<FilmItems>> loader, ArrayList<FilmItems> data) {
        rvSearchResult.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SearchAdapter(getContext());
        adapter.setmData(data);
        rvSearchResult.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<FilmItems>> loader) {
        adapter.clearmData();
    }

    @Override
    public void onDestroy() {
        if(asyncTaskLoader.isStarted())
            asyncTaskLoader.cancelLoad();
        getLoaderManager().destroyLoader(0);
        super.onDestroy();
    }
}
