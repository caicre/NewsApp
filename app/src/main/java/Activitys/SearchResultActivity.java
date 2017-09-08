package Activitys;

import News.News;
import Console.Console;

import java.util.*;

import android.os.Bundle;
import NewsApi.*;
import android.content.Intent;
import android.view.View;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by cai on 2017/9/8.
 */

public class SearchResultActivity extends AppCompatActivity implements NewsActivity{
    private ArrayList<News> newsList;
    private View view;

    public void setNewsList(ArrayList<News> news) {
        newsList = news;
    }

    public void refresh() {
        view.invalidate();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Console console = new Console(this);
        NewsThread runnable = new NewsThread(
                console,
                NewsSearchType.valueOf(intent.getStringExtra("nst")),
                intent.getStringExtra("keyword"),
                intent.getIntExtra("pageNum", 0),
                intent.getIntExtra("pageSize", 0),
                intent.getIntExtra("category", 1)
        );
        Thread thread = new Thread(runnable);
        thread.start();

        //setContentView
    }
}
