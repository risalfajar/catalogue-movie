package com.example.risalfajar.cataloguemovie.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.risalfajar.cataloguemovie.R;
import com.example.risalfajar.cataloguemovie.database.FilmHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.risalfajar.cataloguemovie.database.DatabaseContract.CONTENT_URI;
import static com.example.risalfajar.cataloguemovie.database.DatabaseContract.FilmColumns.IMAGEURL;
import static com.example.risalfajar.cataloguemovie.database.DatabaseContract.getColumnString;

/**
 * Created by Risal Fajar on 6/10/2018.
 */

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private List<String> mWidgetItemsUrl = new ArrayList<>();
    private List<Bitmap> mBitmap = new ArrayList<>();
    private Context mContext;
    private FilmHelper filmHelper;
    private int mAppWidgetId;
    private final HashMap<String, Bitmap> bitmapHashMap;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        filmHelper = new FilmHelper(mContext);
        bitmapHashMap = new HashMap<>();
        int i = 0;
        while (i < mWidgetItemsUrl.size()) {
            mBitmap.add(null);
            i++;
        }
    }

    @Override
    public void onCreate(){
    }

    @Override
    public void onDataSetChanged() {
//        filmHelper.open();
//        ArrayList<FilmItems> filmItems = filmHelper.query();
//        filmHelper.close();
//        mWidgetItemsUrl.clear();
//        for(FilmItems item:filmItems){
//            mWidgetItemsUrl.add(item.getImagePath());
//        }
        final long identityToken = Binder.clearCallingIdentity();

        mWidgetItemsUrl.clear();
        Cursor cursor = mContext.getContentResolver().query(CONTENT_URI, new String[]{IMAGEURL}, null, null, null);
        if(cursor.moveToFirst()){
            do{
                mWidgetItemsUrl.add(getColumnString(cursor, IMAGEURL));
                cursor.moveToNext();
            }while(!cursor.isAfterLast());
        }
        cursor.close();

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return mWidgetItemsUrl.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        Bitmap bitmap = null;
        String url = mWidgetItemsUrl.get(position);

        //Mencegah loading berulang kali, menyimpan bitmap di hashmap
        if(bitmapHashMap.get(url) == null){
            try {
                bitmap = Glide
                        .with(mContext)
                        .load(url)
                        .asBitmap()
                        .error(new ColorDrawable(mContext.getResources().getColor(R.color.gray)))
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                bitmapHashMap.put(url, bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        rv.setImageViewBitmap(R.id.imageView, bitmapHashMap.get(url));

        Bundle extras = new Bundle();
        extras.putInt(FavoritMovieWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
