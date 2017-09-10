package com.example.cai.newsapp.News;

/**
 * Created by YeB on 2017/9/7.
 */

/*
要改善的部分：
1. ADD_NEWS时，若在标题或内容中有双引号,逗号会发生异常（SQLiteDatabase.execSQL()中）
*/
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.widget.Toast;

/**
 * Created by YeB on 2017/9/7.
 */


public class NewsDetailDbHelper extends SQLiteOpenHelper{
    private Context nContext;
    public static final String CREATE_NEWS =  "create table news("+
            "id integer primary key autoincrement,"+"nid text,"+"title text,"
            +"author text,"+"classtag text,"+"time text,"+"intro text,"+
            "pictures text,"+"url text,"+"source text,"+
            "content text,"+"category text,"+"journal text,"+
            "thumbimage blob,"+"isLiked text)";
    //构造方法：第一个参数Context，第二个参数数据库名，第三个参数cursor允许我们在查询数据的时候返回一个自定义的光标位置，一般传入的都是null，第四个参数表示目前库的版本号（用于对库进行升级）
    public NewsDetailDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        nContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //调用SQLiteDatabase中的execSQL（）执行建表语句。
        db.execSQL(CREATE_NEWS);
        Toast.makeText(nContext, "class NewsDetailDbHelper: NewsDetailDb has been created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //如果news表已存在则删除表
        db.execSQL("drop table if exists news");
        onCreate(db);
    }
}
