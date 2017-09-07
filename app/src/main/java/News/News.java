package News;

/**
 * Created by YeB on 2017/9/7.
 */

class News {
    private String id;
    private String title;
    private String author;
    private String classTag;
    private String time;
    private String intro;
    private String []pictures;
    private String url;	//来源网址
    private String source;	//来源
    News(String id, String title, String author, String classTag, String time, String intro,
         String[] pictures, String url, String source){
        this.id = id;
        this.title = title;
        this.author = author;
        this.classTag = classTag;
        this.time = time;
        this.intro = intro;
        this.pictures = pictures;
        this.url = url;
        this.source = source;
    }
    public String getId(){ return id; }
    public String getTitle(){ return title; }
    public String getAuthor(){ return author; }
    public String getClassTag(){ return classTag; }
    public String getTime(){ return time; }
    public String getIntro(){ return intro; }
    public String[] getPictures(){ return pictures; }
    public String getUrl(){ return url; }
    public String getSource(){ return source; }
}
