package com.java.app45;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.java.app45.Console.Console;
import com.java.app45.News.News;
import com.java.app45.NewsApi.NewsSearchType;
import com.java.app45.NewsApi.NewsThread;

/**
 * Created by cai on 2017/9/8.
 */

public class NewsDetailActivity extends AppCompatActivity implements NewsActivity{
    private News news;
    private View view;

    public void refresh(){
        view.invalidate();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Console console = new Console(this);
        NewsThread runnable = new NewsThread(console, NewsSearchType.Id, intent.getStringExtra("ID"));
        Thread thread = new Thread(runnable);
        thread.start();
    }
}