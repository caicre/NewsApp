package com.java.app45;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.java.app45.Console.Console;
import com.java.app45.News.DataConsole;
import com.java.app45.News.NewsDetail;
import com.java.app45.NewsApi.NewsSearchType;
import com.java.app45.NewsApi.NewsThread;
import com.java.app45.weibo.WBAuthActivity;
import com.java.app45.weibo.WeiboShareActivity;
import com.sina.weibo.sdk.share.WbShareHandler;


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
    private DataConsole dConsole;
    private MediaPlayer player = new MediaPlayer();
    private String[] newsDetailSegment;
    private int nowSegment;
    private int segmentNum;
    private String intro;
    //微博分享Handler
    private WbShareHandler shareHandler;

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
            if(dConsole.contain(newsDetail)) {
                Log.d("localRefresh", "start");
                localRefresh(url);
            }
            else {
                Log.d("onlineRefresh", "start");
                onlineRefresh(url);
            }
        }

        newsTitle.setText(newsDetail.getTitle());
        newsContent.setText(newsDetail.getContent());

        int start = 0;
        int segmentLength = 100;
        int end = segmentLength;
        String str = newsDetail.getContent();
        newsDetailSegment = new String[str.length()/segmentLength+1];
        segmentNum = 0;
        while(end < str.length()){
            newsDetailSegment[segmentNum++] = str.substring(start, end);
            start = end;
            end += segmentLength;
        }
        newsDetailSegment[segmentNum++] = str.substring(start);
        nowSegment = 0;
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                           @Override
                                           public void onCompletion(MediaPlayer mp) {
                                               playNextSegment();
                                           }
                                       }
        );

        playNextSegment();

        NewsApplication app = (NewsApplication)getApplication();
        app.filter.weightPlus(newsDetail.getClassTag());

        newsContent.invalidate();
        image.invalidate();
        newsTitle.invalidate();

        //最后把新闻存到数据库里
        newsDetail.setIntro(intro);
        dConsole.addNewsDetail(newsDetail);

        //初始化微博分享Handler
        Log.d("shareHandler: ","start");
        shareHandler = new WbShareHandler(DetailActivity.this);
        shareHandler.registerApp();
        Log.d("shareHandler: ","end");
    }
    private void localRefresh(String[] url){
        image.setImageBitmap(dConsole.loadPicture(getApplicationContext(), url[0]));
        ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
        final ImageView[] imageViews = new ImageView[url.length];
        for (int i = 1; i < imageViews.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setPadding(50, 0, 50, 0);
            imageViews[i] = imageView;
            //imageView.setImageResource(R.drawable.ic_launcher);
            imageView.setImageBitmap(dConsole.loadPicture(getApplicationContext(),url[i]));
            group.addView(imageView);
            group.invalidate();
        }
    }
    private void onlineRefresh(String[] url){
        Glide.with(this).load(url[0]).placeholder(R.drawable.ic_image_loading).into(new GlideDrawableImageViewTargetForSave(image, url[0]));

        ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
        final ImageView[] imageViews = new ImageView[url.length];
        for (int i = 1; i < imageViews.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setPadding(50, 0, 50, 0);
            imageViews[i] = imageView;
            //imageView.setImageResource(R.drawable.ic_launcher);
            Glide.with(this).load(url[i]).placeholder(R.drawable.ic_image_loading).into(new GlideDrawableImageViewTargetForSave(imageView, url[i]));
            group.addView(imageView);
            group.invalidate();
        }
    }
    protected void playNextSegment(){
        if(nowSegment >= segmentNum)
            return;
        try {
            player.reset();
            player.setDataSource("http://tts.baidu.com/text2audio?lan=zh&ie=UTF-8&spd=6&text="+
                    newsDetailSegment[nowSegment++]);
            player.prepare();
            player.start();
        }
        catch (Exception e) {
            Log.e("ERROR", e.toString());
            player = null;
        }
    }

    public void onBackPressed(){
        super.onBackPressed();
        player.release();
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
        dConsole = new DataConsole(getApplicationContext());
        NewsThread runnable = new NewsThread(console, NewsSearchType.Id, intent.getStringExtra("ID"));
        Thread thread = new Thread(runnable);

        newsTitle.setText("新闻标题");
        newsContent.setText("新闻内容");
        intro = intent.getStringExtra("intro");
        thread.start();
    }

    private class GlideDrawableImageViewTargetForSave extends GlideDrawableImageViewTarget {
        private String pictureUrl;

        GlideDrawableImageViewTargetForSave(ImageView imageView, String pictureUrl) {
            super(imageView);
            this.pictureUrl = pictureUrl;
        }

        @Override
        public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
            super.onResourceReady(drawable, anim);
            //在这里添加一些图片加载完成的操作
            Bitmap bitmap = ((GlideBitmapDrawable) drawable.getCurrent()).getBitmap();
            dConsole.savePicture(getApplicationContext(), bitmap, pictureUrl);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_collection_add_delete) {
            if(dConsole.isCollection(newsDetail)) {
                dConsole.deleteCollection(newsDetail);
                Toast.makeText(DetailActivity.this,"删除收藏",Toast.LENGTH_LONG).show();
            }
            else {
                dConsole.addCollection(newsDetail);
                Toast.makeText(DetailActivity.this,"添加收藏",Toast.LENGTH_LONG).show();
            }
            return true;
        }
        if (itemThatWasClickedId == R.id.action_share) {
            Intent i = new Intent(DetailActivity.this, WeiboShareActivity.class);
            i.putExtra(WeiboShareActivity.KEY_SHARE_TYPE, WeiboShareActivity.SHARE_CLIENT);
            i.putExtra("nid", newsDetail.getId());
            startActivity(i);
            Toast.makeText(DetailActivity.this,"微博分享",Toast.LENGTH_LONG).show();
            return true;
        }
        if (itemThatWasClickedId == R.id.action_get_access) {
            startActivity(new Intent(DetailActivity.this, WBAuthActivity.class));
            Toast.makeText(DetailActivity.this,"微博授权",Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
