package com.example.cai.newsapp.NewsApi;

import java.net.*;
import java.io.*;
import android.util.Log;
import android.os.Handler;
import android.os.Message;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.cai.newsapp.Console.Console;

/**
 * Created by cai on 2017/9/7.
 */

public class NewsThread implements Runnable {

    public static final int science = 1;
    public static final int education = 2;
    public static final int military = 3;
    public static final int national = 4;
    public static final int society = 5;
    public static final int culture = 6;
    public static final int vehicle = 7;
    public static final int international = 8;
    public static final int sports = 9;
    public static final int finance = 10;
    public static final int health = 11;
    public static final int entertainment = 12;

    private Console console;
    private NewsSearchType nst;
    private String keyword;
    private int pageNum;
    private int pageSize;
    private int category;
    private String ID;

    private Handler handler;

    //最近新闻
    public NewsThread(Console console, NewsSearchType nst, int pageNum, int pageSize, int category) {
        this.handler = console.handler;
        this.console = console;
        this.nst = nst;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.category = category;
    }

    //按关键词查找新闻
    public NewsThread(Console console, NewsSearchType nst, String keyword, int pageNum, int pageSize, int category) {
        this.handler = console.handler;
        this.console = console;
        this.nst = nst;
        this.keyword = keyword;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.category = category;
    }

    //按ID得到新闻详细内容，或按地址得到图片
    public NewsThread(Console console, NewsSearchType nst, String ID) {
        this.handler = console.handler;
        this.console = console;
        this.nst = nst;
        this.ID = ID;
    }

    public void run() {
        StringBuffer urlStr = new StringBuffer("http://166.111.68.66:2042/news/action/query/");
        switch(nst) {
            case Latest:
                urlStr.append("latest?").
                        append("pageNo="+String.valueOf(pageNum)).
                        append("&pageSize="+String.valueOf(pageSize)).
                        append("&category="+String.valueOf(category));
                break;
            case Keyword:
                urlStr.append("search?").
                        append("keyword="+keyword);

                if(pageNum != 0) {
                    urlStr.append("pageNo="+String.valueOf(pageNum)).
                            append("&pageSize="+String.valueOf(pageSize)).
                            append("&category="+String.valueOf(category));
                }
                break;
            case Id:
                urlStr.append("detail?newsId="+ID);
                break;
            case Picture:
                urlStr = new StringBuffer(ID);
        }

        try {
            URL url = new URL(urlStr.toString());
            if(nst == NewsSearchType.Picture) {
                URLConnection con = url.openConnection();
                con.setDoInput(true);
                con.connect();
                InputStream is = con.getInputStream();
                Bitmap pic = BitmapFactory.decodeStream(is);
                is.close();

                console.CallBackBitmap(pic, ID);
            }
            else {
                BufferedReader in = new BufferedReader(new
                        InputStreamReader(url.openStream()));
                String inputLine;
                if((inputLine = in.readLine()) != null) {
                    console.CallBackStr(inputLine);
                    Log.i("NewsThread", inputLine);
                }
                in.close();
            }
        }
        catch(MalformedURLException e) {
            Log.i("NewsThread", "MalformedURLException");
        }
        catch(IOException e) {
            Log.i("NewsThread", "IOException");
            e.printStackTrace();
        }

        Message msg = new Message();
        msg.what = Console.refresh;
        handler.sendMessage(msg);
    }
}
