package News;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.util.ArrayList;
/**
 * Created by YeB on 2017/9/7.
 */

public class NewsDetailDbConsole {
    private NewsDetailDbHelper dbHelper;
    NewsDetailDbConsole(AppCompatActivity a) {
        dbHelper = new NewsDetailDbHelper(a, "NewsDetail.db", null, 1); // a要改为"类名.this"吗？
    }
    private ContentValues getContentValues (NewsDetail n){
        ContentValues values = new ContentValues();
        values.put("id", n.getId());
        values.put("title", n.getTitle());
        values.put("author", n.getAuthor());
        values.put("classtag", n.getClassTag());
        values.put("time", n.getTime());
        values.put("intro", n.getIntro());
        values.put("url", n.getUrl());
        values.put("source", n.getSource());
        values.put("content", n.getContent());
        values.put("category", n.getContent());
        values.put("journal", n.getJournal());
        String[] list = n.getPictureSrc();
        String temp ="";
        int i=0;
        for(;i<list.length-1; i++)
            temp += list[i]+";";
        temp += list[i];
        values.put("pictures", temp);
        return values;
    }
    private NewsDetail getColumns(Cursor cursor){
        NewsDetail n;
        String id = cursor.getString(cursor.getColumnIndex("id"));
        String title = cursor.getString(cursor.getColumnIndex("title"));
        String author = cursor.getString(cursor.getColumnIndex("author"));
        String classTag = cursor.getString(cursor.getColumnIndex("classtag"));
        String time = cursor.getString(cursor.getColumnIndex("time"));
        String intro = cursor.getString(cursor.getColumnIndex("intro"));
        String[] pictures = cursor.getString(cursor.getColumnIndex("pictures")).split(";");
        String url = cursor.getString(cursor.getColumnIndex("url"));
        String source = cursor.getString(cursor.getColumnIndex("source"));
        String content = cursor.getString(cursor.getColumnIndex("content"));
        String category = cursor.getString(cursor.getColumnIndex("category"));
        String journal = cursor.getString(cursor.getColumnIndex("journal"));
        n = new NewsDetail(id,title, author, classTag, time, intro, pictures, url, source, content, category, journal);
        return n;
    }
    public void createDB() {
        //创建或打开数据库
        dbHelper.getWritableDatabase();
    }
    public void addNews(NewsDetail n){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = getContentValues(n);
        values.put("isLiked","false");
        db.insert("news", null, values);
        values.clear();
    }
    public void addNews(NewsDetail n, boolean isLiked){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = getContentValues(n);
        if(isLiked)
            values.put("isLiked","true");
        else
            values.put("isLiked","false");
        db.insert("news", null, values);
        values.clear();
    }
    public void deleteNews(String id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("news", "id=?", new String[]{id});
    }
    public NewsDetail findNews(String mid){
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            //指明去查询news表。
            String table = "news";
            String[] columns = null; //所有列
            String selection = "id=?";
            String[] selectionArgs = new String[]{ mid };
            String groupBy = null;
        String having = null;
        String orderBy = null;
        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        //调用moveToFirst()将数据指针移动到第一行的位置。
        NewsDetail n = null;
        if(cursor.moveToFirst()){
            do {
                //然后通过Cursor的getColumnIndex()获取某一列中所对应的位置的索引
                n = getColumns(cursor);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return n;
    }
    public ArrayList<NewsDetail> getNewsList(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //指明去查询news表。
        ArrayList<NewsDetail> list = new ArrayList<NewsDetail>();
        String table = "news";
        String[] columns = null; //所有列
        String selection = null;
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;
        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        //调用moveToFirst()将数据指针移动到第一行的位置。
        NewsDetail n = null;
        if(cursor.moveToFirst()){
            do {
                //然后通过Cursor的getColumnIndex()获取某一列中所对应的位置的索引
                n = getColumns(cursor);
                list.add(n);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public ArrayList<NewsDetail> getCollectionList(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //指明去查询news表。
        ArrayList<NewsDetail> list = new ArrayList<NewsDetail>();
        String table = "news";
        String[] columns = null; //所有列
        String selection = "isLiked=?";
        String[] selectionArgs = new String[]{ "true" };
        String groupBy = null;
        String having = null;
        String orderBy = null;
        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        //调用moveToFirst()将数据指针移动到第一行的位置。
        NewsDetail n = null;
        if(cursor.moveToFirst()){
            do {
                //然后通过Cursor的getColumnIndex()获取某一列中所对应的位置的索引
                n = getColumns(cursor);
                list.add(n);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
