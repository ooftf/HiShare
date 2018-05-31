package com.ooftf.hishare;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class TencentShare {
    private static Application application;
    private static String appId;
    public static void init(Application application, String appId){
        TencentShare.appId =appId;
        TencentShare.application =application;
    }
    private static Tencent tencentInstance;
    public static Tencent getTencentInstance() {
        if (tencentInstance == null) {
            tencentInstance = Tencent.createInstance(appId, application);
        }
        return tencentInstance;
    }
    public static void shareQQ(final Activity activity, final HiShare.ShareParams shareParam, final ShareCallback callback) {
        if(!getTencentInstance().isQQInstalled(application)){
            if(callback!=null){
                callback.onError(HiShare.shareType,ShareCallback.ErrorCode.QQ_UNINSTALLED);
            }
            return;
        }
        if(shareParam.imageUrl == null&&shareParam.bitmap!=null){
            BitmapUtils.pathFromBitmap(shareParam.bitmap).subscribe(new Observer<String>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(String s) {
                    shareReal(activity,shareParam,s,callback);
                }

                @Override
                public void onError(Throwable e) {
                    callback.onError(HiShare.shareType,ShareCallback.ErrorCode.SAVE_BITMAP);
                }

                @Override
                public void onComplete() {

                }
            });
        }else{
            shareReal(activity,shareParam,null,callback);
        }
    }
    static IUiListener uiListener;
    static void shareReal(Activity activity, HiShare.ShareParams shareParam, String imageLocalUrl, final ShareCallback callback){
        Bundle bundle = new Bundle();
        //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_SUMMARY不能全为空，最少必须有一个是有值的。
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, shareParam.title);
        //分享的图片URL
        if(imageLocalUrl != null){
            bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imageLocalUrl);
        }
        if(shareParam.imageUrl != null){
            bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareParam.imageUrl);
        }
        //这条分享消息被好友点击后的跳转URL。
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareParam.url);
        //分享的消息摘要，最长50个字
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareParam.content);
        uiListener = new IUiListener() {
            @Override
            public void onComplete(Object o) {
                if(callback!=null){
                    callback.onSuccess(HiShare.shareType);
                }
            }

            @Override
            public void onError(UiError uiError) {
                if(callback!=null){
                    callback.onError(HiShare.shareType,ShareCallback.ErrorCode.UNKNOWN);
                }
            }

            @Override
            public void onCancel() {
                if(callback!=null){
                    callback.onCancel();
                }
            }
        };
        getTencentInstance().shareToQQ(activity, bundle, uiListener);
    }
    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_QQ_SHARE&uiListener!=null) {
            Tencent.onActivityResultData(requestCode,resultCode,data,uiListener);
            uiListener = null;
        }
    }
}
