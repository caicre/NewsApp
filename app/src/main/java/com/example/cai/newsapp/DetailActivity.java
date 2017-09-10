package com.example.cai.newsapp;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import com.example.cai.newsapp.Console.Console;
import com.example.cai.newsapp.News.NewsDetail;
import com.example.cai.newsapp.NewsApi.NewsSearchType;
import com.example.cai.newsapp.NewsApi.NewsThread;

import static android.R.id.list;

/**
 * Created by lenovo on 9/9/2017.
 */

public class DetailActivity extends AppCompatActivity implements NewsActivity{
    private ProgressBar mProgressBar;
    private TextView newsContent;
    private ImageView image;
    CollapsingToolbarLayout collapsingToolbar;
    private TextView newsTitle;
    private Console console;
    private NewsDetail newsDetail;

    public void setNewsDetail(NewsDetail newsDetail){
        this.newsDetail = newsDetail;
    }

    @Override
    public void refresh() {

        String[] url = newsDetail.getPictures();

        if(url[0].equals("") || url == null) {
            image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.news_pic));
        }
        else {
            Glide.with(this).load(url[0]).placeholder(R.drawable.ic_image_loading).into(image);

            ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
            ImageView[] imageViews = new ImageView[url.length];
            for (int i = 1; i < imageViews.length; i++) {
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imageView.setPadding(50, 0, 50, 0);
                imageViews[i] = imageView;
                //imageView.setImageResource(R.drawable.ic_launcher);
                Glide.with(this).load(url[i]).placeholder(R.drawable.ic_image_loading).into(imageView);
                group.addView(imageView);
                group.invalidate();
            }
        }

        newsTitle.setText(newsDetail.getTitle());
        newsContent.setText(newsDetail.getContent());

        newsContent.invalidate();
        image.invalidate();
        newsTitle.invalidate();
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        newsContent = (TextView) findViewById(R.id.NewsContent);
        image = (ImageView) findViewById(R.id.ivImage);
        newsTitle = (TextView) findViewById(R.id.NewsTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");

        Intent intent = getIntent();
        console = new Console(this);
        NewsThread runnable = new NewsThread(console, NewsSearchType.Id, intent.getStringExtra("ID"));
        Thread thread = new Thread(runnable);

        newsTitle.setText("新闻标题");
        newsContent.setText("新闻内容");

        thread.start();
    }


}
