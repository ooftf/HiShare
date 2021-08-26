package com.ooftf.hishare.sina;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;

import com.ooftf.hishare.HiShare;
import com.ooftf.hishare.ISharePlatform;
import com.ooftf.hishare.ShareCallback;
import com.ooftf.hishare.WbTempActivity;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.common.UiError;
import com.sina.weibo.sdk.openapi.IWBAPI;
import com.sina.weibo.sdk.openapi.WBAPIFactory;
import com.sina.weibo.sdk.share.WbShareCallback;


public class WbPlatform implements ISharePlatform {
    public static WbPlatform current;
    private static Application application;
    private static String appId;
    private  ShareCallback callback;
    private static IWBAPI iwbapi;

    public static void init(Application application, String appId) {
        WbPlatform.appId = appId;
        WbPlatform.application = application;
        iwbapi =  WBAPIFactory.createWBAPI(application);
        iwbapi.registerApp(WbPlatform.application, new AuthInfo(WbPlatform.application, appId, "https://api.weibo.com/oauth2/default.html", ""));
    }
    HiShare.ShareParams shareParam;
    @Override
    public void share(int shareType, HiShare.ShareParams shareParam, ShareCallback callback) {
        current = this;
        this.callback = callback;
        this.shareParam = shareParam;
        Intent intent = new Intent(application, WbTempActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        application.startActivity(intent);
    }

    public void shareReal() {

        WeiboMultiMessage message = new WeiboMultiMessage();
        message.textObject = new TextObject();
        message.textObject.text = shareParam.content;
        message.textObject.title = shareParam.title;
        message.textObject.actionUrl = shareParam.targetUrl;
        message.imageObject = new ImageObject();
        if (!TextUtils.isEmpty(shareParam.imageUrl)) {
            message.imageObject.imagePath = shareParam.imageUrl;
        }
        if (shareParam.bitmap != null) {
            message.imageObject.setImageData(shareParam.bitmap);
        }
        iwbapi.shareMessage(message, false);
    }

    public  void onNewIntent(Intent intent) {
        if (iwbapi == null) {
            return;
        }
        iwbapi.doResultIntent(intent, new WbShareCallback() {
            @Override
            public void onComplete() {
                if (callback != null) {
                    callback.onSuccess(HiShare.shareType);
                    callback = null;
                }
            }

            @Override
            public void onError(UiError uiError) {
                if (callback != null) {
                    callback.onError(HiShare.shareType, ShareCallback.ErrorCode.UNKNOWN);
                    callback = null;
                }
            }

            @Override
            public void onCancel() {
                if (callback != null) {
                    callback.onCancel(HiShare.shareType);
                    callback = null;
                }
            }
        });
    }
}
