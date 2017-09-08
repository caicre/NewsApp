package com.example.cai.newsapp;

import NewsApi.NewsSearchType;
import NewsApi.NewsThread;
import News.*;
import Console.Console;
import Activitys.*;

import java.util.*;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NewsActivity {

    private View view;
    private SwipeRefreshLayout swipeRefresh;
    private String key="0";
    private Console console;
    private ArrayList<News> newsList;

    public void refresh(){
        view.invalidate();
    }

    public void setNewsList(ArrayList<News> news){
        newsList = news;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = (TextView) findViewById(R.id.news_title_list);
        SwipeRefresh();
        console = new Console(this);
        NewsThread runnable = new NewsThread(console, NewsSearchType.Latest, 1, 10, NewsThread.science);
        //NewsThread runnable = new NewsThread(console, NewsSearchType.Picture, "http://img003.21cnimg.com/photos/album/20160808/m600/A3B78A702DF9BF0EE02ADFD5D4F53D54.jpeg");
        Thread thread = new Thread(runnable);
        thread.start();

       contentToView();
    }

    //点击搜索按钮，启动SearchResultActivity，在该activity中访问网络
    public void changeToSearchActivity(NewsSearchType nst, String keyword, int pageNum, int pageSize, int category) {
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra("nst", nst.toString());
        intent.putExtra("keyword", keyword);
        intent.putExtra("pageNum", pageNum);
        intent.putExtra("pageSize", pageSize);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    public void changeNewsDetailActivity(String ID) {
        Intent intent = new Intent(this, NewsDetailActivity.class);
        intent.putExtra("ID", ID);
        startActivity(intent);
    }

    //这里需要实现将newsList的东西放到view中
    private void contentToView() {
    }

    public void SwipeRefresh(){
        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipeLayout);
        swipeRefresh.setColorSchemeResources(R.color.blue);
        swipeRefresh.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefresh.setProgressBackgroundColor(R.color.colorPrimary);
        swipeRefresh.setProgressViewEndTarget(true, 200);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                contentToView();
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);          //打开页面时调用，将对应的xml文件与menu类绑定
        return true;
    }
}
