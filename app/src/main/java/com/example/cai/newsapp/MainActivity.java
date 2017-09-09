package com.example.cai.newsapp;

import NewsApi.NewsSearchType;
import NewsApi.NewsThread;
import News.*;
import Console.Console;
import Activitys.*;

/*
import java.util.*;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NewsActivity {

    private View view;
    private SwipeRefreshLayout swipeRefresh;
    private String key="0";

    public void refresh(){
        view.invalidate();
    }

    public void setNewsList(ArrayList<News> news){
        newsList = news;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = (TextView) findViewById(R.id.news_title_list);
        SwipeRefresh();
        

       contentToView();
    }

    //点击搜索按钮，启动SearchResultActivity，在该activity中访问网络
    public void changeToSearchActivity(NewsSearchType nst, String keyword, int pageNum, int pageSize, int category) {
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra("nst", nst.toString());
        intent.putExtra("keyword", keyword);
        intent.putExtra("pageNum", pageNum);
        intent.putExtra("pageSize", pageSize);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    public void changeNewsDetailActivity(String ID) {
        Intent intent = new Intent(this, NewsDetailActivity.class);
        intent.putExtra("ID", ID);
        startActivity(intent);
    }

    //这里需要实现将newsList的东西放到view中
    private void contentToView() {
    }

    public void SwipeRefresh(){
        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipeLayout);
        swipeRefresh.setColorSchemeResources(R.color.blue);
        swipeRefresh.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefresh.setProgressBackgroundColor(R.color.colorPrimary);
        swipeRefresh.setProgressViewEndTarget(true, 200);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                contentToView();
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);          //打开页面时调用，将对应的xml文件与menu类绑定
        return true;
    }
}
*/

import java.util.*;


import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity implements NewsActivity {
    private static final int newsNumPer = 10;
    private int pageNum;
    private int category;
    private ListView mListView;
    private SwipeRefreshLayout mSwipe;
    private int picNum;

    private Console console;
    private ArrayList<News> newsList;

    ListViewAdapter adapt;

    final String click_Load_More="点击加载更多";
    final String loading_Load_More="加载中...";
    final String comp_Load_More="没有更多";
    String nowNormalText = "";
    boolean isLoading = false;  //是否正在加载
    boolean isComp = false;     //是否加载完成


    RelativeLayout id_rl_loading;
    TextView id_pull_to_refresh_loadmore_text;
    ProgressBar id_pull_to_refresh_load_progress;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsList = new ArrayList<News>();
        adapt = new ListViewAdapter(newsList);
        console = new Console(this);
        pageNum = 1;
        picNum = 1;
        category = NewsThread.science;

        mListView = (ListView) findViewById(R.id.listLayout);
        mListView.setAdapter(adapt);                                 //设置接收器


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {            //设置点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dec = String.format("点击了第%d个新闻",position+1);
                Toast.makeText(MainActivity.this,dec,Toast.LENGTH_LONG).show();

            }
        });


        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){         //设置长按事件
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                String dec = String.format("长按了第%d个新闻",position+1);
                Toast.makeText(MainActivity.this,dec,Toast.LENGTH_LONG).show();
                return true;
            }
        });

//设置点击加载更多↓
        View listview_footer_view = LayoutInflater.from(this).inflate(R.layout.listview_footer, null);
        mListView.addFooterView(listview_footer_view);
        id_rl_loading= (RelativeLayout) listview_footer_view.findViewById(R.id.id_rl_loading);
        id_pull_to_refresh_load_progress = (ProgressBar) listview_footer_view.findViewById(R.id.id_pull_to_refresh_load_progress);
        id_pull_to_refresh_load_progress.setVisibility(View.GONE);
        id_pull_to_refresh_loadmore_text = (TextView) listview_footer_view.findViewById(R.id.id_pull_to_refresh_loadmore_text);
        nowNormalText = click_Load_More;
        id_pull_to_refresh_loadmore_text.setClickable(true);
        id_pull_to_refresh_loadmore_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMore();
            }
        });
//点击加载更多设置完成↑
        loadMore();
        initSwipeLayout();
    }

    private void initSwipeLayout(){
        mSwipe = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        mSwipe.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mSwipe.setProgressViewOffset(false, 0, 25);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshData();
                        adapt.notifyDataSetChanged();
                        mSwipe.setRefreshing(false);
                    }
                }, 3 * 1000);
            }
        });
    }

    class ViewHolder{
        ImageView image;
        TextView title;
        TextView intro;
    }

    public class ListViewAdapter extends BaseAdapter{
        ArrayList<News> newsList;
        private MainActivity activity;

        public void setActivity(MainActivity activity) {
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
            LayoutInflater inflater = (LayoutInflater)MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(convertView == null){
                convertView = inflater.inflate(R.layout.item_news, null);
                holder = new ViewHolder();

                holder.title = (TextView) convertView.findViewById(R.id.newsTitle);
                holder.image = (ImageView) convertView.findViewById(R.id.newsImage);
                holder.intro = (TextView) convertView.findViewById(R.id.newsIntro);
                convertView.setTag(holder);
            }

            holder=(ViewHolder)convertView.getTag();
            News news=newsList.get(position);                                   //将holder与目标内容关联
            holder.title.setText(news.getTitle());
            Glide.with(activity).load(activity.getNewsPicUrl(activity.picNum)).into(holder.image);
            //holder.image.setImageBitmap(news.getThumb());
            holder.intro.setText(news.getIntro());
            return convertView;
        }
    }

    private void refreshData(){
        /*newsList.clear();
        for(int i=0;i<15;i++){

            News news = new News();
            news.setTitle("New CardView"+i);
            news.setImgId(i%3==0?R.drawable.ic_image_loadfail:R.drawable.ic_image_loading);
            news.setIntro("New CardView "+i+" is here");
            newsList.add(news);
        }
*/
    }

    private void loadMore(){
        id_rl_loading.setVisibility(View.VISIBLE);
        id_pull_to_refresh_loadmore_text.setText(loading_Load_More);
        id_pull_to_refresh_loadmore_text.setClickable(false);
        id_pull_to_refresh_load_progress.setVisibility(View.VISIBLE);
        isLoading = true;
        picNum += newsNumPer;

        new Handler().postDelayed(new Runnable() {
            @Override


            public void run() {
                loadData();
            }
        }, 3 * 1000);

    }
    private void loadData(){

        //Log.i("XXX","loadData");
        NewsThread runnable = new NewsThread(console, NewsSearchType.Latest, pageNum++, newsNumPer, category);
        //NewsThread runnable = new NewsThread(console, NewsSearchType.Picture, "http://img003.21cnimg.com/photos/album/20160808/m600/A3B78A702DF9BF0EE02ADFD5D4F53D54.jpeg");
        Thread thread = new Thread(runnable);
        thread.start();

     /*   for(int i=0;i<15;i++){
            News news = new News();
            news.setTitle("More CardView"+i);
            news.setImgId(R.drawable.ic_image_loadfail);
            news.setIntro("More CardView "+i+" is here");
            newsList.add(news);
        }*/
        //adapt.notifyDataSetChanged();
        isLoading = false;
        id_pull_to_refresh_loadmore_text.setText(click_Load_More);
        id_pull_to_refresh_loadmore_text.setClickable(true);
        id_pull_to_refresh_load_progress.setVisibility(View.GONE);
    }

    //点击搜索按钮，启动SearchResultActivity，在该activity中访问网络
    public void changeToSearchActivity(NewsSearchType nst, String keyword, int pageNum, int pageSize, int category) {
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra("nst", nst.toString());
        intent.putExtra("keyword", keyword);
        intent.putExtra("pageNum", pageNum);
        intent.putExtra("pageSize", pageSize);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    public void changeNewsDetailActivity(String ID) {
        Intent intent = new Intent(this, NewsDetailActivity.class);
        intent.putExtra("ID", ID);
        startActivity(intent);
    }
}
