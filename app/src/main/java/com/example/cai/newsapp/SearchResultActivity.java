package com.example.cai.newsapp;

import com.example.cai.newsapp.News.News;
import com.example.cai.newsapp.Console.Console;

import java.util.*;

import android.os.Bundle;
import com.example.cai.newsapp.NewsApi.*;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by cai on 2017/9/8.
 */

public class SearchResultActivity extends AppCompatActivity implements NewsActivity{

    final String click_Load_More="加载中...";
    final String loading_Load_More="加载中...";
    String nowNormalText = "";
    boolean isLoading = false;  //是否正在加载

    private ListView mListView;


    private ArrayList<News> newsList;
    private View view;

    public void addNewsList(ArrayList<News> news) {
        newsList.addAll(news);
    }

    public void refresh() {
        view.invalidate();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsList = new ArrayList<News>();
        
        setContentView(R.layout.search_result);
        setToolbar();
        mListView = (ListView) findViewById(R.id.mlistLayout);

        /*Intent intent = getIntent();
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
        thread.start();*/




        /*            这一段取消注释即可获得上拉刷新效果，MainActivity中initSwipeLayout()相关全部去除
        View listview_footer_view = LayoutInflater.from(this).inflate(R.layout.listview_footer, null);
        mListView.addFooterView(listview_footer_view,null,false);
        id_rl_loading= (RelativeLayout) listview_footer_view.findViewById(R.id.id_rl_loading);
        id_pull_to_refresh_load_progress = (ProgressBar) listview_footer_view.findViewById(R.id.id_pull_to_refresh_load_progress);
        id_pull_to_refresh_load_progress.setVisibility(View.GONE);
        id_pull_to_refresh_loadmore_text = (TextView) listview_footer_view.findViewById(R.id.id_pull_to_refresh_loadmore_text);
        nowNormalText = click_Load_More;

        mListView.setOnScrollListener(new AbsListView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        loadMore();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }

        });*/

    }

    private void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.mtoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
