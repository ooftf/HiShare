package com.ooftf.hishare.sina;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.support.v4.app.ShareCompat;
import android.text.TextUtils;

import com.ooftf.hishare.HiShare;
import com.ooftf.hishare.ISharePlatform;
import com.ooftf.hishare.ShareCallback;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;

public class WbPlatform implements ISharePlatform {
    private static Application application;
    private static String appId;
    private static ShareCallback callback;
    private static WbShareHandler shareHandler;

    public static void init(Application application, String appId) {
        WbPlatform.appId = appId;
        WbPlatform.application = application;
        WbSdk.install(WbPlatform.application,new AuthInfo(WbPlatform.application,appId,"https://api.weibo.com/oauth2/default.html",""));
    }

    @Override
    public void share(Activity activity, int shareType, HiShare.ShareParams shareParam, ShareCallback callback) {
        shareReal(activity, shareParam, callback);
    }

    private void shareReal(Activity activity, HiShare.ShareParams shareParam, ShareCallback callback) {
        WbPlatform.callback = callback;
        shareHandler = new WbShareHandler(activity);
        shareHandler.registerApp();
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
            message.imageObject.setImageObject(shareParam.bitmap);
        }
        shareHandler.shareMessage(message, false);
    }

    public static void onNewIntent(Intent intent) {
        if (shareHandler == null) return;
        shareHandler.doResultIntent(intent, new WbShareCallback() {
            @Override
            public void onWbShareSuccess() {
                if (callback != null) {
                    callback.onSuccess(HiShare.shareType);
                    callback = null;
                }
            }

            @Override
            public void onWbShareCancel() {
                if (callback != null) {
                    callback.onCancel(HiShare.shareType);
                    callback = null;
                }
            }

            @Override
            public void onWbShareFail() {
                if (callback != null) {
                    callback.onError(HiShare.shareType, ShareCallback.ErrorCode.UNKNOWN);
                    callback = null;
                }
            }
        });
        shareHandler = null;
    }
}
