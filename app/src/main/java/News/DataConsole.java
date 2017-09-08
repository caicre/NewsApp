package News;

/**
 * Created by YeB on 2017/9/7.
 */

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.regex.*;
import java.util.ArrayList;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.regex.*;
import java.util.ArrayList;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.regex.*;
import java.util.ArrayList;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.regex.*;
import java.util.ArrayList;

public class DataConsole {

    //string to NewsDetail
    static public NewsDetail toNewsDetail(String newsStr){
        //注意: info部分为空， 以后调用toNewsDetail时要另外处理info成员变量
        Pattern pClassTag = Pattern.compile("\"newsClassTag\".*?:.*?\"(.*?)\"");
        Pattern pAuthor = Pattern.compile("\"news_Author\".*?:.*?\"(.*?)\"");
        Pattern pId = Pattern.compile("\"news_ID\".*?:.*?\"(.*?)\"");
        Pattern pPictures = Pattern.compile("\"news_Pictures\".*?:.*?\"(.*?)\"");
        Pattern pSource = Pattern.compile("\"news_Source\".*?:.*?\"(.*?)\"");
        Pattern pTime = Pattern.compile("\"news_Time\".*?:.*?\"(.*?)\"");
        Pattern pTitle = Pattern.compile("\"news_Title\".*?:.*?\"(.*?)\"");
        Pattern pUrl = Pattern.compile("\"news_URL\".*?:.*?\"(.*?)\"");
        Pattern pContent = Pattern.compile("\"news_Content\".*?:.*?\"(.*?)\"");
        Pattern pCategory = Pattern.compile("\"news_Category\".*?:.*?\"(.*?)\"");
        Pattern pJournal = Pattern.compile("\"news_Journal\".*?:.*?\"(.*?)\"");
        Matcher m;
        String classTag, author, id, source, time, title, url, content, category, journal;
        String[] pictures;
        m = pClassTag.matcher(newsStr); m.find(); classTag = m.group(1);
        m = pAuthor.matcher(newsStr); m.find(); author = m.group(1);
        m = pId.matcher(newsStr); m.find(); id = m.group(1);
        m = pPictures.matcher(newsStr); m.find(); pictures = m.group(1).split(";");
        m = pSource.matcher(newsStr); m.find(); source = m.group(1);
        m = pTime.matcher(newsStr); m.find(); time = m.group(1);
        m = pTitle.matcher(newsStr); m.find(); title = m.group(1);
        m = pUrl.matcher(newsStr); m.find(); url = m.group(1);
        m = pContent.matcher(newsStr); m.find(); content = m.group(1);
        m = pCategory.matcher(newsStr); m.find(); category = m.group(1);
        m = pJournal.matcher(newsStr); m.find(); journal = m.group(1);

        NewsDetail n = new NewsDetail(id, title, author, classTag, time, "", pictures, url, source, content, category, journal);

        return n;
    }

    //string to News
    static public News toNews(String newsStr) {
        Pattern pClassTag = Pattern.compile("\"newsClassTag\":\"(.*?)\"");
        Pattern pAuthor = Pattern.compile("\"news_Author\":\"(.*?)\"");
        Pattern pId = Pattern.compile("\"news_ID\":\"(.*?)\"");
        Pattern pPictures = Pattern.compile("\"news_Pictures\":\"(.*?)\"");
        Pattern pSource = Pattern.compile("\"news_Source\":\"(.*?)\"");
        Pattern pTime = Pattern.compile("\"news_Time\":\"(.*?)\"");
        Pattern pTitle = Pattern.compile("\"news_Title\":\"(.*?)\"");
        Pattern pUrl = Pattern.compile("\"news_URL\":\"(.*?)\"");
        Pattern pIntro = Pattern.compile("\"news_Intro\":\"(.*?)\"");
        Matcher m;
        String classTag, author, id, source, time, title, url, intro;
        String[] pictures;
        m = pClassTag.matcher(newsStr); m.find(); classTag = m.group(1);
        m = pAuthor.matcher(newsStr); m.find(); author = m.group(1);
        m = pId.matcher(newsStr); m.find(); id = m.group(1);
        m = pPictures.matcher(newsStr); m.find(); pictures = m.group(1).split(";");
        m = pSource.matcher(newsStr); m.find(); source = m.group(1);
        m = pTime.matcher(newsStr); m.find(); time = m.group(1);
        m = pTitle.matcher(newsStr); m.find(); title = m.group(1);
        m = pUrl.matcher(newsStr); m.find(); url = m.group(1);
        m = pIntro.matcher(newsStr); m.find(); intro = m.group(1);
        News n = new News(id, title, author, classTag, time, intro, pictures, url, source);
        return n;
    }

    //page string to NewsArr
    static public ArrayList<News> toNewsArr(String str) {
        ArrayList<News> news = new ArrayList<News>();
        Pattern p1 = Pattern.compile("\\{(.+?)\\}");
        Matcher m = p1.matcher(str);

        while(m.find()) {
            News n = toNews(m.group(1));
            news.add(n);
        }
        return news;
    }

}

