package com.java.app45;

import android.app.Application;

import com.java.app45.NewsApi.NewsFilter;

/**
 * Created by cai on 2017/9/13.
 */

public class NewsApplication extends Application {
    public NewsFilter filter;
    public int pageSiize;
    public void onCreate() {
        pageSiize = 15;
        filter = new NewsFilter(pageSiize);
        super.onCreate();
    }
}
