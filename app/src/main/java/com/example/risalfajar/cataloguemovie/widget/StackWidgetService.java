package com.example.risalfajar.cataloguemovie.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Risal Fajar on 6/10/2018.
 */

public class StackWidgetService extends RemoteViewsService{

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(getApplicationContext(), intent);
    }
}
