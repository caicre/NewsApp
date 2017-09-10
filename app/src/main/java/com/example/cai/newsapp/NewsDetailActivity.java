package com.example.cai.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.LinkedList;

import com.example.cai.newsapp.Console.Console;
import com.example.cai.newsapp.News.News;
import com.example.cai.newsapp.NewsApi.NewsSearchType;
import com.example.cai.newsapp.NewsApi.NewsThread;

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