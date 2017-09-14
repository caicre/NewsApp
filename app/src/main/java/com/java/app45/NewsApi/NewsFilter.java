package com.java.app45.NewsApi;

import com.java.app45.News.*;
import java.util.*;

/**
 * Created by cai on 2017/9/13.
 */

public class NewsFilter {
    private String noKeyword;
    private int[] typeWeight = new int[12];
    private int[] newsNum = new int[12];
    private int sum =  12;
    private int returnNewsNum = 0;

    public int stringTypeToInt(String type){
        int i = 0;
        switch(type) {
            case "科技":
                i = 1;
                break;
            case "教育":
                i = 2;
                break;
            case "军事":
                i = 3;
                break;
            case "国内":
                i = 4;
                break;
            case "社会":
                i = 5;
                break;
            case "文化":
                i = 6;
                break;
            case "汽车":
                i = 7;
                break;
            case "国际":
                i = 8;
                break;
            case "体育":
                i = 9;
                break;
            case "财经":
                i = 10;
                break;
            case "健康":
                i = 11;
                break;
            case "娱乐":
                i = 12;
                break;
        }
        return i;
    }

    public void weightPlus(String type){
        int i = stringTypeToInt(type) - 1;
        if(i < 0 || i > 11)
            return;
        typeWeight[i]++;
        sum++;
    }

    public NewsFilter(int returnNewsNum){
        this.returnNewsNum = returnNewsNum;
        for(int i = 0; i < 12; i++) {
            typeWeight[i]++;
        }
    }

    public NewsFilter(String noKeyword, int returnNewsNum){
        this.noKeyword = noKeyword;
        this.returnNewsNum = returnNewsNum;
        for(int i = 0; i < 12; i++) {
            typeWeight[i]++;
        }
    }

    public ArrayList<News> getNews(ArrayList<News> newsList) {
        class NumPair{
            public int index;
            public int value;
        }
        class NumPairComparator implements Comparator<NumPair> {
            public int compare(NumPair a, NumPair b) {
                return b.value - a.value;
            }
        }

        NumPair[] newsNumPair = new NumPair[12];
        for(int i = 0; i < 12; i++) {
            newsNum[i] = (int)((float)typeWeight[i]/(float)sum * returnNewsNum);
            newsNumPair[i] = new NumPair();
            newsNumPair[i].index = i;
            newsNumPair[i].value = newsNum[i];
        }

        //降序排列
        NumPairComparator comparator = new NumPairComparator();
        Arrays.sort(newsNumPair, comparator);
        int temp = returnNewsNum;
        for(int i = 0; i < 12; i++) {
            temp -= newsNum[i];
        }

        for(int i = 0; i < temp; i++) {
            newsNumPair[i].value++;
        }
        for(int i = 0; i < 12; i++) {
            newsNum[newsNumPair[i].index] = newsNumPair[i].value;
        }

        ArrayList<News> ans = new ArrayList<News>(newsList.size());
        int[] nowNewsNum = new int[12];
        int nowNum = 0;
        for(News news: newsList){
            int i = stringTypeToInt(news.getClassTag()) - 1;
            if(nowNewsNum[i] < newsNum[i]) {
                ans.add(news);
                nowNum++;
                if(nowNum == returnNewsNum)
                    break;
            }
        }

        return ans;
    }
}
