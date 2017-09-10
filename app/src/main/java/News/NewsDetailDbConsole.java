package News;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
/**
 * Created by YeB on 2017/9/7.
 */

public class NewsDetailDbConsole {
    private NewsDetailDbHelper dbHelper;
    static final int HISTORY_NUM = 10;
    NewsDetailDbConsole(Context context) {
        dbHelper = new NewsDetailDbHelper(context, "NewsDetail.db", null, 1); // a要改为"类名.this"吗？
    }
    private ContentValues getContentValues (NewsDetail n){
        ContentValues values = new ContentValues();
        values.put("nid", n.getId());
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
        values.put("thumbimage", bmpToByteArray(n.getThumb()));
        String[] list = n.getPictures();
        String temp ="";
        int i=0;
        for(;i<list.length-1; i++)
            temp += list[i]+";";
        temp += list[i];
        values.put("pictures", temp);
        return values;
    }
    private byte[] bmpToByteArray(Bitmap bmp) {
        // Default size is 32 bytes  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if(bmp == null)
            return null;
        try {  
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }  
    private NewsDetailLiked getColumns(Cursor cursor){
        NewsDetailLiked n;
        String nid = cursor.getString(cursor.getColumnIndex("nid"));
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
        n = new NewsDetailLiked(nid,title, author, classTag, time, intro, pictures, url, source, content, category, journal);
        if(cursor.getString(cursor.getColumnIndex("isLiked")).toLowerCase().equals("true"))
            n.setIsLiked(true);
        else
            n.setIsLiked(false);

        byte[] data = cursor.getBlob(cursor.getColumnIndex("thumbimage"));
        //没有thumbImage, 直接以null来替代
        if(data.length != 0) {
            Bitmap thumbImage = BitmapFactory.decodeByteArray(data, 0, data.length);
            n.setThumb(thumbImage);
        }
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
    public void setNewsIsLiked(NewsDetail n, boolean isLiked){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //指明去查询news表。
        String table = "news";
        String selection = "nid=?";
        String[] selectionArgs = new String[]{ n.getId() };
        ContentValues values = getContentValues(n);
        if(isLiked)
            values.put("isLiked", "true");
        else
            values.put("isLiked","false");
        //调用moveToFirst()将数据指针移动到第一行的位置。
        db.update(table, values, selection, selectionArgs);
    }
    public void deleteNews(String nid){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("news", "nid=?", new String[]{ nid });
    }
    public NewsDetail findNews(String nid){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //指明去查询news表。
        String table = "news";
        String[] columns = null; //所有列
        String selection = "nid=?";
        String[] selectionArgs = new String[]{ nid };
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
    //获取浏览历史或获取收藏新闻(false:浏览历史 true:收藏新闻)
    public ArrayList<News> getNewsList(boolean collection){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //指明去查询news表。
        ArrayList<News> list = new ArrayList<News>();
        String table = "news";
        String[] columns = null; //所有列
        //获取最近新闻(HISTORY_NUM 个)
        if(!collection) {
            String selection = null;
            String[] selectionArgs = null;
            String groupBy = null;
            String having = null;
            String orderBy = "id DESC";
            Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
            //调用moveToFirst()将数据指针移动到第一行的位置。
            NewsDetail n = null;
            int i = HISTORY_NUM;
            if (cursor.moveToFirst()) {
                do {
                    //然后通过Cursor的getColumnIndex()获取某一列中所对应的位置的索引
                    n = getColumns(cursor);
                    list.add(n);
                    i--;
                } while (cursor.moveToNext() && i>HISTORY_NUM);
            }
            cursor.close();
        }
        //获取收藏新闻(所有)
        else{
            String selection = "isLiked=?";
            String[] selectionArgs = new String[]{ "true" };
            String groupBy = null;
            String having = null;
            String orderBy = "id DESC";
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
        }
        return list;
    }

    public void clear() {
        dbHelper.close();
    }
}
