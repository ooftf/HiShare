package com.ooftf.hishare.sina;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;

public class WBShareActivity extends Activity implements WbShareCallback{
    WbShareHandler shareHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shareHandler = new WbShareHandler(this);
        shareHandler.registerApp();
        WeiboMultiMessage message = new WeiboMultiMessage();
        message.textObject = getTextObject();
        message.imageObject = getImageObject();
        shareHandler.shareMessage(message,false);
    }

    private ImageObject getImageObject() {
        ImageObject imageObject = new ImageObject();
        imageObject.imagePath="www.baidu.com";
        return new ImageObject();
    }

    private TextObject getTextObject() {
        TextObject textObject = new TextObject();
        textObject.text = "text";
        textObject.title = "title";
        return  textObject;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        shareHandler.doResultIntent(intent,this);
    }

    @Override
    public void onWbShareSuccess() {
        finish();
    }

    @Override
    public void onWbShareCancel() {
        finish();
    }

    @Override
    public void onWbShareFail() {
        finish();
    }
}
