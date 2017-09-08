package News;

import android.graphics.Bitmap;

/**
 * Created by YeB on 2017/9/7.
 */

public class NewsDetail extends News{
    private String content;
    private String category;
    private String journal; //记者
    private String[] pictureSrc;//图片存储地址
    private Bitmap[] picture;
    NewsDetail(String id, String title, String author, String classTag, String time, String intro,
               String[] pictures, String url, String source, String content, String category, String journal)
    {
        super(id, title, author, classTag, time, intro, pictures, url, source);
        this.content = content;
        this.category = category;
        this.journal = journal;
    }
    public String getContent(){ return content; }
    public String getCategory(){ return category; }
    public String getJournal() { return journal; }
    public String[] getPictureSrc(){ return pictureSrc; }
}
