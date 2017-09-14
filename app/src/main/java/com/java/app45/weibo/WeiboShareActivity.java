package com.java.app45.weibo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.java.app45.News.DataConsole;
import com.java.app45.News.NewsDetail;
import com.java.app45.R;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MultiImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;

import java.io.File;
import java.util.ArrayList;

public class WeiboShareActivity extends Activity implements WbShareCallback {
    public static final String KEY_SHARE_TYPE = "key_share_type";
    public static final int SHARE_CLIENT = 1;
    /** 界面标题 */
    private TextView mTitleView;
    /** 分享图片 */
    private ImageView mImageView;
    private DataConsole dConsole;
    private WbShareHandler shareHandler;
    private int mShareType = SHARE_CLIENT;
    private NewsDetail newsDetail;
    int flag = 0;
    /**
     * @see {@link Activity#onCreate}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dConsole = new DataConsole(getApplicationContext());
        mShareType = getIntent().getIntExtra(KEY_SHARE_TYPE, SHARE_CLIENT);
        newsDetail = dConsole.findNews(getApplicationContext(), getIntent().getStringExtra("nid"));
        shareHandler = new WbShareHandler(this);
        shareHandler.registerApp();
        shareHandler.setProgressColor(0xff33b5e5);

        flag = 1;
        sendMessage(true, true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        shareHandler.doResultIntent(intent,this);
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     */
    private void sendMessage(boolean hasText, boolean hasImage) {
        sendMultiMessage(hasText, hasImage);
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     */
    private void sendMultiMessage(boolean hasText, boolean hasImage) {
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if (hasText) {
            weiboMessage.textObject = getTextObj();
        }
        if (hasImage) {
            weiboMessage.multiImageObject = getMultiImageObject();
        }
        shareHandler.shareMessage(weiboMessage, false);
    }



    @Override
    public void onWbShareSuccess() {
        Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();
        WeiboShareActivity.this.finish();
    }

    @Override
    public void onWbShareFail() {
        Toast.makeText(this,
                "分享失败" + "Error Message: ", Toast.LENGTH_LONG).show();
        WeiboShareActivity.this.finish();
    }

    @Override
    public void onWbShareCancel() {
        Toast.makeText(this, "取消分享", Toast.LENGTH_LONG).show();
        WeiboShareActivity.this.finish();
    }


    /**
     * 创建文本消息对象。
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = newsDetail.getTitle()+"\n\n"+newsDetail.getContent();
        textObject.title = newsDetail.getTitle();
        //textObject.actionUrl = "http://www.baidu.com";
        return textObject;
    }

    /**
     * 创建图片消息对象。
     * @return 图片消息对象。
     */

    private ImageObject getImageObj() {
        ImageObject imageObject = new ImageObject();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.news_pic);
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    /***
     * 创建多图
     * @return
     */
    private MultiImageObject getMultiImageObject(){
        MultiImageObject multiImageObject = new MultiImageObject();
        //pathList设置的是本地本件的路径,并且是当前应用可以访问的路径，现在不支持网络路径（多图分享依靠微博最新版本的支持，所以当分享到低版本的微博应用时，多图分享失效
        // 可以通过WbSdk.hasSupportMultiImage 方法判断是否支持多图分享,h5分享微博暂时不支持多图）多图分享接入程序必须有文件读写权限，否则会造成分享失败
        ArrayList<Uri> pathList = new ArrayList<Uri>();
        for(String pictureUrl : newsDetail.getPictures())
            if(pictureUrl !="") {
                pathList.add(Uri.fromFile(new File(getFilesDir() + "/" + dConsole.toFileName(pictureUrl))));
            }
        multiImageObject.setImageList(pathList);
        return multiImageObject;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
