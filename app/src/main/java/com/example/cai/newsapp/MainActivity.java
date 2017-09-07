package com.example.cai.newsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
}
