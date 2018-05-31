package com.ooftf.hishare;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.Toast;

/**
 * 这一层与业务没有关系，只提供分享功能
 */
public class HiShare {
    static Application application;
    static int shareType;
    static Converter<String, Bitmap> urlToBitmap;

    public static void init(Application application, Converter<String, Bitmap> urlToBitmap) {
        HiShare.application = application;
        HiShare.urlToBitmap = urlToBitmap;
    }

    public static void initWXShare(String appId) {
        WXShare.init(application, appId);
    }

    public static void initTencentShare(String appId) {
        TencentShare.init(application, appId);
    }

    public interface ShareType {
        int WX_FAVORITE = 0;//微信收藏
        int WX_FRIEND = 1;//微信好友
        int WX_MOMENT = 2;//微信朋友圈
        int QQ_FRIEND = 3;//QQ好友
    }

    public static void share(Activity activity, int shareType, ShareParams shareParam, ShareCallback callback) {
        HiShare.shareType = shareType;
        switch (shareType) {
            case ShareType.QQ_FRIEND:
                TencentShare.shareQQ(activity, shareParam, callback);
                return;
            case ShareType.WX_FAVORITE:
                WXShare.shareFavorite(activity, shareParam, callback);
                return;
            case ShareType.WX_FRIEND:
                WXShare.shareSession(activity, shareParam, callback);
                return;
            case ShareType.WX_MOMENT:
                WXShare.shareTimeline(activity, shareParam, callback);
                return;
        }
    }

    public static class ShareParams {
        public ShareParams(String url, String title, String content, String imageUrl, Bitmap bitmap) {
            this.url = url;
            this.title = title;
            this.content = content;
            this.imageUrl = imageUrl;
            this.bitmap = bitmap;
        }

        public String url;
        public String title;
        public String content;
        public String imageUrl;
        public Bitmap bitmap;
    }

    /**
     * 用于qq分享的回调,如果分享的activity没有调用这个方法就会导致qq分享响应不到回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        TencentShare.onActivityResult(requestCode, resultCode, data);
    }

    public static class DefaultShareCallback implements ShareCallback {

        @Override
        public void onError(int shareType, int code) {
            switch (code) {
                case ErrorCode.WX_UNINSTALLED:
                    Toast.makeText(application, "没有检测到微信应用，请安装后再试", Toast.LENGTH_SHORT).show();
                    break;
                case ErrorCode.QQ_UNINSTALLED:
                    Toast.makeText(application, "没有检测到QQ应用，请安装后再试", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(application, "分享失败", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onSuccess(int shareType) {
            Toast.makeText(application, "分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(application, "分享取消", Toast.LENGTH_SHORT).show();
        }
    }

    interface Converter<S, T> {
        T convert(S url);
    }
}
