package com.example.cai.newsapp;

import NewsApi.NewsSearchType;
import NewsApi.NewsThread;
import Interface.CallBack;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView newsTitleList;
    private SwipeRefreshLayout swipeRefresh;
    private String key="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SwipeRefresh();
        Console console = new Console();
        NewsThread runnable = new NewsThread(console, NewsSearchType.Latest, 1, 10, NewsThread.science);
        Thread thread = new Thread(runnable);
        thread.start();

        newsTitleList = (TextView) findViewById(R.id.news_title_list);
        newsTitleList.append(console.str);
    }
    public void SwipeRefresh(){
        newsTitleList = (TextView) findViewById(R.id.news_title_list);

        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipeLayout);
        swipeRefresh.setColorSchemeResources(R.color.blue);
        swipeRefresh.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefresh.setProgressBackgroundColor(R.color.colorPrimary);
        swipeRefresh.setProgressViewEndTarget(true, 200);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                Console console = new Console();
                NewsThread runnable = new NewsThread(console, NewsSearchType.Latest, 1, 10, NewsThread.science);
                Thread thread = new Thread(runnable);
                thread.start();
                try{
                    Thread.sleep(1500);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                newsTitleList = (TextView) findViewById(R.id.news_title_list);
                newsTitleList.clearComposingText();
                newsTitleList.append(console.str);
                swipeRefresh.setRefreshing(false);
                //mHandler.sendEmptyMessage(1);


            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);          //打开页面时调用，将对应的xml文件与menu类绑定
        return true;
    }
}

class Console implements CallBack {
    public String str;

    public Console() {
        str = new String("news!");
    }

    public void CallBackStr(String str){
        this.str = str;

    }
}
