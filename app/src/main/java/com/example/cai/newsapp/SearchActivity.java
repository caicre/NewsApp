package com.example.cai.newsapp;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import static android.R.id.list;

public class SearchActivity extends AppCompatActivity{
    private SearchView mSearchView;
    Button Btn[] = new Button[13];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        mSearchView = (SearchView) findViewById(R.id.search);


       /* Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
*/
//开始创建多个button
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.Relative);
        for(int i=0;i<=12;i++){
            Btn[i] = new Button(this);
            Btn[i].setId(i);
            Btn[i].setText("按钮"+(i+1));
            //Btn[i].setBackgroundDrawable(R.drawable.button_style);
            Btn[i].setBackgroundColor(Color.GRAY);
            RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams(400,100);
            btParams.leftMargin = 10;
            btParams.topMargin = 20+110*i;

            layout.addView(Btn[i],btParams);
        }

        for(int k=0;k<Btn.length;k++){
            Btn[k].setTag(k);
            Btn[k].setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {                      //多个按钮按钮的监听事件
                    int i = (Integer) v.getTag();   //这里的i不能在外部定义
                    changeColor(i);

                }
            });
        }
        //Button列表创建完成

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {   // 搜索框的监听

            @Override
            public boolean onQueryTextSubmit(String query) {                          //当点击搜索按钮时触发该方法，query时搜索框的内容
                Toast.makeText(SearchActivity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }
    private void changeColor(int i){                            //第i个按钮的监听事件
        Btn[i].setBackgroundColor(Color.BLUE);
    }
}
