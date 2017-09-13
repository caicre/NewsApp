package com.java.app45;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import static android.R.id.list;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

/**
 * Created by lenovo on 9/9/2017.
 */

public class TextDetailActivity extends AppCompatActivity{
    private TextView newsContent;
    private TextView newsTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail_text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.text_toolbar);


        newsContent = (TextView) findViewById(R.id.TextContent);
        newsTitle = (TextView) findViewById(R.id.TextTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        newsTitle.setText("新闻标题");
        newsContent.setText("新闻内容");
    }


}
