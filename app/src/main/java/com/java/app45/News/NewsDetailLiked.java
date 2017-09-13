package com.java.app45.News;

/**
 * Created by YeB on 2017/9/10.
 */

public class NewsDetailLiked extends NewsDetail {
    private boolean isLiked;
    NewsDetailLiked(String id, String title, String author, String classTag, String time, String intro,
               String[] pictures, String url, String source, String content, String category, String journal){
        super(id, title, author, classTag, time, intro, pictures, url, source, content, category, journal);
        isLiked = false;
    }
    public void setIsLiked(boolean isLiked){ this.isLiked = isLiked; }
    public boolean getIsLiked(){ return isLiked; }
}
