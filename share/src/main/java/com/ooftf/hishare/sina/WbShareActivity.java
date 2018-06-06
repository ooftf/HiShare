package com.ooftf.hishare.sina;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.ooftf.hishare.HiShare;
import com.ooftf.hishare.ShareCallback;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;

public class WbShareActivity extends Activity implements WbShareCallback {
    interface IntentExtra {
        String TARTGET_URLL = "targetUrl";
        String TITLE = "title";
        String CONTENT = "content";
        String IMAGE_URL = "imageUrl";
        String BITMAP = "bitmap";
    }

    public static void getStartIntent(Context context, String targetUrl, String title, String content, String imageUrl, Bitmap bitmap) {
        Intent intent = new Intent(context, WbShareActivity.class);
        intent.putExtra(IntentExtra.TARTGET_URLL, targetUrl);
        intent.putExtra(IntentExtra.TITLE, title);
        intent.putExtra(IntentExtra.CONTENT, content);
        intent.putExtra(IntentExtra.IMAGE_URL, imageUrl);
        intent.putExtra(IntentExtra.BITMAP, bitmap);
    }

    String targetUrl = null;
    String title = null;
    String content = null;
    String imageUrl = null;
    Bitmap bitmap = null;

    void findIntentExtra(Intent intent) {
        targetUrl = intent.getStringExtra(IntentExtra.TARTGET_URLL);
        title = intent.getStringExtra(IntentExtra.TITLE);
        content = intent.getStringExtra(IntentExtra.CONTENT);
        imageUrl = intent.getStringExtra(IntentExtra.IMAGE_URL);
        bitmap = intent.getParcelableExtra(IntentExtra.BITMAP);
    }

    WbShareHandler shareHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shareHandler = new WbShareHandler(this);
        shareHandler.registerApp();
        WeiboMultiMessage message = new WeiboMultiMessage();
        message.textObject = getTextObject();
        message.imageObject = getImageObject();
        shareHandler.shareMessage(message, false);
    }

    private ImageObject getImageObject() {
        ImageObject imageObject = new ImageObject();
        if (!TextUtils.isEmpty(imageUrl)) {
            imageObject.imagePath = imageUrl;
        }
        if (bitmap != null) {
            imageObject.setImageObject(bitmap);
        }
        return new ImageObject();
    }

    private TextObject getTextObject() {
        TextObject textObject = new TextObject();
        textObject.text = content;
        textObject.title = title;
        textObject.actionUrl = targetUrl;
        return textObject;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        shareHandler.doResultIntent(intent, this);
    }

    @Override
    public void onWbShareSuccess() {
        if (WbPlatform.callback != null) {
            WbPlatform.callback.onSuccess(HiShare.shareType);
            WbPlatform.callback = null;
        }
        finish();
    }

    @Override
    public void onWbShareCancel() {
        if (WbPlatform.callback != null) {
            WbPlatform.callback.onCancel(HiShare.shareType);
            WbPlatform.callback = null;
        }
        finish();
    }

    @Override
    public void onWbShareFail() {
        if (WbPlatform.callback != null) {
            WbPlatform.callback.onError(HiShare.shareType, ShareCallback.ErrorCode.UNKNOWN);
            WbPlatform.callback = null;
        }
        finish();
    }
}
