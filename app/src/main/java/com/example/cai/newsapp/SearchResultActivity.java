package com.example.cai.newsapp;

import com.example.cai.newsapp.News.News;
import com.example.cai.newsapp.Console.Console;

import java.util.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import com.example.cai.newsapp.NewsApi.*;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by cai on 2017/9/8.
 */

public class SearchResultActivity extends AppCompatActivity implements NewsActivity{

    final String click_Load_More="加载中...";
    final String loading_Load_More="加载中...";
    String nowNormalText = "";
    boolean isLoading = false;  //是否正在加载
    RelativeLayout id_rl_loading;
    TextView id_pull_to_refresh_loadmore_text;
    ProgressBar id_pull_to_refresh_load_progress;
    private static final int newsNumPer = 10;
    private int pageNum;
    private String keyword;
    private int category;

    private ListView mListView;

    private ArrayList<News> newsList;
    private View view;
    private Console console;
    com.example.cai.newsapp.SearchResultActivity.ListViewAdapter2 adapt;

    int mPosition = -1;

    public void addNewsList(ArrayList<News> news) {
        newsList.addAll(news);
    }

    public void refresh() {
        mListView.invalidate();
        adapt.notifyDataSetChanged();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsList = new ArrayList<News>();

        setContentView(R.layout.search_result);
        setToolbar();

        adapt = new com.example.cai.newsapp.SearchResultActivity.ListViewAdapter2(newsList);
        adapt.setActivity(this);
        mListView = (ListView) findViewById(R.id.mlistLayout);
        mListView.setAdapter(adapt);                                 //设置接收器

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {            //设置点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(com.example.cai.newsapp.SearchResultActivity.this, DetailActivity.class);
                intent.putExtra("ID", newsList.get(position).getId());
                startActivity(intent);

                mPosition = position;
                adapt.notifyDataSetChanged();
                Toast.makeText(com.example.cai.newsapp.SearchResultActivity.this,"进入详情"+position,Toast.LENGTH_LONG).show();

            }
        });

        pageNum = 1;
        Intent intent = getIntent();
        String str = intent.getStringExtra("nst");
        console = new Console(this);
        keyword = intent.getStringExtra("keyword");
        category = intent.getIntExtra("category", 0);
        NewsThread runnable = new NewsThread(
                console,
                NewsSearchType.Keyword,
                keyword,
                pageNum++,
                newsNumPer,
                category
        );
        Thread thread = new Thread(runnable);
        thread.start();

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

        });

    }

    public class ListViewAdapter2 extends BaseAdapter {
        ArrayList<News> newsList;
        private com.example.cai.newsapp.SearchResultActivity activity;

        public void setActivity(com.example.cai.newsapp.SearchResultActivity activity) {
            this.activity = activity;
        }

        public ListViewAdapter2(ArrayList<News> newsList) {
            this.newsList = newsList;
        }

        public int getCount() {
            return newsList.size();
        }

        public Object getItem(int position) {
            return newsList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            com.example.cai.newsapp.SearchResultActivity.ViewHolder holder = null;
            LayoutInflater inflater = (LayoutInflater) com.example.cai.newsapp.SearchResultActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_news, null);
                holder = new com.example.cai.newsapp.SearchResultActivity.ViewHolder();

                holder.title = (TextView) convertView.findViewById(R.id.newsTitle);
                holder.image = (ImageView) convertView.findViewById(R.id.newsImage);
                holder.intro = (TextView) convertView.findViewById(R.id.newsIntro);
                convertView.setTag(holder);
            }

            holder = (com.example.cai.newsapp.SearchResultActivity.ViewHolder) convertView.getTag();
            News news = newsList.get(position);                                   //将holder与目标内容关联
            holder.title.setText(news.getTitle());
            Bitmap bitmap = null;
            if (news.getThumb() == null)
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.news_pic);
            else
                bitmap = news.getThumb();
            holder.image.setImageBitmap(bitmap);
            holder.intro.setText(news.getIntro());

            if (mPosition == -1) {
                holder.title.setEnabled(false);
            } else if (mPosition == position) {
                holder.title.setEnabled(true);
            }

            return convertView;
        }
    }

    class ViewHolder{
        ImageView image;
        TextView title;
        TextView intro;
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

    private void loadMore(){
        id_rl_loading.setVisibility(View.VISIBLE);
        id_pull_to_refresh_loadmore_text.setText(loading_Load_More);
        id_pull_to_refresh_load_progress.setVisibility(View.VISIBLE);
        isLoading = true;

        new Handler().postDelayed(new Runnable() {
            @Override


            public void run() {
                loadData();
            }
        }, 3 * 1000);
    }

    private void loadData(){
        NewsThread runnable = new NewsThread(
                console,
                NewsSearchType.Keyword,
                keyword,
                pageNum++,
                newsNumPer,
                category
        );
        Thread thread = new Thread(runnable);
        thread.start();

        mPosition = -1;
        isLoading = false;
        id_pull_to_refresh_loadmore_text.setText(click_Load_More);
        id_pull_to_refresh_load_progress.setVisibility(View.GONE);
    }
}
