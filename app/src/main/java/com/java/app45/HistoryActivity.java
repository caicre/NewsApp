package com.java.app45;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.java.app45.News.DataConsole;
import com.java.app45.News.News;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity implements NewsActivity {
    private ListView mListView;
    private DataConsole dConsole;

    private ArrayList<News> newsList;

    ListViewAdapter adapt;

    @Override
    public void refresh(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        setToolbar();

        dConsole = new DataConsole(getApplicationContext());
        newsList = dConsole.getNewsHistory(getApplicationContext());
        adapt = new ListViewAdapter(newsList);
        adapt.setActivity(this);

        mListView = (ListView) findViewById(R.id.history_list);
        mListView.setAdapter(adapt);                                 //设置接收器

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {            //设置点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //position是点击了第position个item
                Intent intent=new Intent(HistoryActivity.this, DetailActivity.class);
                intent.putExtra("ID", newsList.get(position).getId());
                startActivity(intent);

                //mPosition = position;??
                adapt.notifyDataSetChanged();
                Toast.makeText(HistoryActivity.this,"进入详情"+position,Toast.LENGTH_LONG).show();
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){         //设置长按事件,不需要内容
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){

                return true;
            }
        });

        loadMore();             //向newsList添加新闻条目
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

            //读取开始
			holder.title.setText(news.getTitle());
            Bitmap bitmap = null;
            if(news.getThumb() == null)
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.news_pic);
            else
                bitmap = news.getThumb();
            holder.image.setImageBitmap(bitmap);
            holder.intro.setText(news.getIntro());
			//读取结束

            //??????????????????
            holder.title.setEnabled(false);

            return convertView;
        }
    }



    private void loadMore(){
        //把新闻列表读取到newslist中
        adapt.notifyDataSetChanged();  //提醒条目更新
    }

    private void setToolbar(){
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
}
