package com.example.cai.newsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView newsTitleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsTitleList = (TextView) findViewById(R.id.news_title_list);
        newsTitleList.append("新闻标题列表\n\n");
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);          //打开页面时调用，将对应的xml文件与menu类绑定
        return true;
    }
}
