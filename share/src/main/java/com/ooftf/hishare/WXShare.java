package com.ooftf.hishare;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/*
* 发送到聊天界面——WXSceneSession

发送到朋友圈——WXSceneTimeline

添加到微信收藏——WXSceneFavorite
* */
public class WXShare {
    private static IWXAPI wxapiInstance;
    private static Application application;
    private static String appId;

    public static void init(Application application, String appId) {
        WXShare.appId = appId;
        WXShare.application = application;
    }

    public static IWXAPI getWxapiInstance() {
        if (wxapiInstance == null) {
            wxapiInstance = WXAPIFactory.createWXAPI(application, appId, false);
        }
        return wxapiInstance;
    }

    private static void share(Activity activity, final HiShare.ShareParams shareParam, final int scene) {
        if(!getWxapiInstance().isWXAppInstalled()){
            if(callback!=null){
                callback.onError(HiShare.shareType,ShareCallback.ErrorCode.WX_UNINSTALLED);
                callback=null;
            }
            return;
        }
        if (shareParam.imageUrl != null) {//如果是网络图片
            BitmapUtils.loadImage(shareParam.imageUrl).subscribe(new Observer<Bitmap>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Bitmap bitmap) {
                    shareParam.bitmap = bitmap;
                    shareReal(shareParam, scene);
                }

                @Override
                public void onError(Throwable e) {
                    if (callback != null) {
                        callback.onError(HiShare.shareType,ShareCallback.ErrorCode.LOAD_IMAGE);
                        callback = null;
                    }
                }

                @Override
                public void onComplete() {

                }
            });
        } else {
            shareReal(shareParam, scene);
        }

    }

    private static void shareReal(HiShare.ShareParams shareParam, int scene) {
        WXWebpageObject webPage = new WXWebpageObject();
        webPage.webpageUrl = shareParam.url;
        WXMediaMessage msg = new WXMediaMessage(webPage);
        msg.title = shareParam.title;
        msg.description = shareParam.content;
        if (shareParam.bitmap.getWidth() > 100 || shareParam.bitmap.getHeight() > 100) {
            Bitmap thumbBmp = Bitmap.createScaledBitmap(shareParam.bitmap, 100, 100, true);
            msg.setThumbImage(thumbBmp);
        } else {
            msg.setThumbImage(shareParam.bitmap);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = scene;
        getWxapiInstance().sendReq(req);
    }

    static ShareCallback callback;
    //分享给好友
    public static void shareSession(Activity activity, HiShare.ShareParams shareParam, ShareCallback callback) {
        WXShare.callback = callback;
        share(activity, shareParam, SendMessageToWX.Req.WXSceneSession);
    }
    //分享到朋友圈
    public static void shareTimeline(Activity activity, HiShare.ShareParams shareParam, ShareCallback callback) {
        WXShare.callback = callback;
        WXShare.share(activity, shareParam, SendMessageToWX.Req.WXSceneTimeline);
    }
    //收藏
    public static void shareFavorite(Activity activity, HiShare.ShareParams shareParam, ShareCallback callback) {
        WXShare.callback = callback;
        WXShare.share(activity, shareParam, SendMessageToWX.Req.WXSceneFavorite);
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
