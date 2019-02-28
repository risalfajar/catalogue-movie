package com.example.risalfajar.cataloguemovie;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.risalfajar.cataloguemovie.entity.FilmItems;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Risal Fajar on 4/26/2018.
 */

public class FilmAsyncTaskLoader extends AsyncTaskLoader<ArrayList<FilmItems>> {

    private ArrayList<FilmItems> filmList;
    private boolean hasResult = false;
    private String filmTitle, url;
    private Context context;

    private static final String API_KEY = BuildConfig.API_KEY;
    public static final String URL_SEARCH = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=en-US&query=";
    public static final String URL_NOW_PLAYING = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + API_KEY + "&language=en-US";
    public static final String URL_UPCOMING = "https://api.themoviedb.org/3/movie/upcoming?api_key=" + API_KEY + "&language=en-US";

    public FilmAsyncTaskLoader(final Context context, String url) {
        super(context);
        this.context = context;
        this.url = url;
        onContentChanged();
    }

    public FilmAsyncTaskLoader(final Context context, String url, String filmTitle) {
        super(context);
        this.context = context;
        this.url = url;
        this.filmTitle = filmTitle;
        onContentChanged();
    }

    @Override
    public void deliverResult(ArrayList<FilmItems> data) {
        filmList = data;
        hasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if(takeContentChanged())
            forceLoad();
        else if(hasResult)
            deliverResult(filmList);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if(hasResult){
            filmList = null;
            hasResult = false;
        }
    }

    @Override
    public ArrayList<FilmItems> loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();
        final ArrayList<FilmItems> filmItemses = new ArrayList<>();

        if(url == URL_SEARCH) {
            url = url + filmTitle;
        }

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    if(url == URL_NOW_PLAYING || url == URL_UPCOMING){
                        JSONArray resultList = responseObject.getJSONArray("results");

                        if(result.length() > 0){
                            for(int i = 0; i < resultList.length(); i++){
                                JSONObject film = resultList.getJSONObject(i);
                                if(film != null){
                                    FilmItems filmItems = new FilmItems(film);
                                    filmItemses.add(filmItems);
                                }
                            }
                        }
                    }else{
                        if(responseObject.getInt("total_results") > 0){
                            JSONArray resultList = responseObject.getJSONArray("results");

                            for (int i = 0; i < resultList.length(); i++){
                                JSONObject film = resultList.getJSONObject(i);
                                if(film != null){
                                    FilmItems filmItems = new FilmItems(film);
                                    filmItemses.add(filmItems);
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
        return filmItemses;
    }
}
