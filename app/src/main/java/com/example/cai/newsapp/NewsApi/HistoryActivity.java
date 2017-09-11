package com.example.cai.newsapp.NewsApi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cai.newsapp.Console.Console;
import com.example.cai.newsapp.News.DataConsole;
import com.example.cai.newsapp.News.News;
import com.example.cai.newsapp.NewsActivity;
import com.example.cai.newsapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by lenovo on 9/11/2017.
 */

public class HistoryActivity extends AppCompatActivity implements NewsActivity {
    private ListView mListView;
    private ArrayList<News> newsList;
    private Console console;
    private DataConsole dConsole;

    private static final int newsNumPer = 10;
    private int pageNum;
    private int category;
    private int picNum;

    ListViewAdapter adapt;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        setToolBar();

        newsList = new ArrayList<News>();
        adapt = new ListViewAdapter(newsList);
        adapt.setActivity(this);
        console = new Console(this);
        dConsole = new DataConsole(getApplicationContext());
        pageNum = 1;
        picNum = 1;
        category = NewsThread.science;

        mListView = (ListView) findViewById(R.id.history_list);
        mListView.setAdapter(adapt);

        loadMore();

    }



    class ViewHolder{
        ImageView image;
        TextView title;
        TextView intro;
    }

    public class ListViewAdapter extends BaseAdapter {
        ArrayList<News> newsList;
        private HistoryActivity activity;

        public void setActivity(HistoryActivity activity) {
            this.activity = activity;
        }

        public ListViewAdapter(ArrayList<News> newsList){
            this.newsList = newsList;
        }

        public int getCount(){
            return newsList.size();
        }

        public Object getItem(int position){
            return newsList.get(position);
        }

        public long getItemId(int position){
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent){
            ViewHolder holder = null;
            LayoutInflater inflater = (LayoutInflater)HistoryActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(convertView == null){
                convertView = inflater.inflate(R.layout.item_news, null);
                holder = new ViewHolder();

                holder.title = (TextView) convertView.findViewById(R.id.newsTitle);
                holder.image = (ImageView) convertView.findViewById(R.id.newsImage);
                holder.intro = (TextView) convertView.findViewById(R.id.newsIntro);
                convertView.setTag(holder);
            }

            holder=(ViewHolder)convertView.getTag();
            News news = newsList.get(position);                                   //将holder与目标内容关联
            holder.title.setText(news.getTitle());
            Bitmap bitmap = null;
            if(news.getThumb() == null)
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.news_pic);
            else
                bitmap = news.getThumb();
            holder.image.setImageBitmap(bitmap);
            holder.intro.setText(news.getIntro());
            holder.title.setEnabled(false);

            return convertView;
        }
    }

    private void setToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.htoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void loadMore(){
        picNum += newsNumPer;

        new Handler().postDelayed(new Runnable() {
            @Override

            public void run() {
                loadData();
                refresh();
            }
        }, 3 * 1000);
    }
    private void loadData(){

        NewsThread runnable = new NewsThread(console, NewsSearchType.Latest, pageNum++, newsNumPer, category);
        //NewsThread runnable = new NewsThread(console, NewsSearchType.Picture, "http://img003.21cnimg.com/photos/album/20160808/m600/A3B78A702DF9BF0EE02ADFD5D4F53D54.jpeg");
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public String getNewsPicUrl(int index) {
        return  newsList.get(index).getPictures()[0];
    }

    public void refresh(){
        adapt.notifyDataSetChanged();
    }

    public void setNewsList(ArrayList<News> news){
        newsList = news;
    }

    public void addNewsList(ArrayList<News> news) {
        newsList.addAll(news);
    }

}
