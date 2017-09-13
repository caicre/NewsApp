package com.java.app45.News;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by YeB on 2017/9/7.
 */

public class NewsDetail extends News{
    private String content;
    private String category;
    private String journal; //记者
    public ArrayList<Bitmap> pictureData;   //图片
    public NewsDetail(String id, String title, String author, String classTag, String time, String intro,
               String[] pictures, String url, String source, String content, String category, String journal)
    {
        super(id, title, author, classTag, time, intro, pictures, url, source);
        this.content = content;
        this.category = category;
        this.journal = journal;
        this.pictureData = new ArrayList<Bitmap>(pictures.length);
    }
    public String getContent(){ return content; }
    public String getCategory(){ return category; }
    public String getJournal() { return journal; }
    public ArrayList<Bitmap> getPictureData() { return pictureData; }
    public void addPictureData(Bitmap bitmap) { pictureData.add(bitmap); }

}
