package com.ooftf.hishare;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.ooftf.hishare.sina.WbPlatform;
import com.ooftf.hishare.tencent.TencentPlatform;
import com.ooftf.hishare.wx.WxPlatform;

/**
 * 这一层与业务没有关系，只提供分享功能
 */
public class HiShare {
    static Application application;
    public static int shareType;

    public static void init(Application application) {
        HiShare.application = application;
    }

    public static void initWXShare(String appId) {
        WxPlatform.init(application, appId);
    }

    public static void initTencentShare(String appId) {
        TencentPlatform.init(application, appId);
    }
    public static void initWbShare(String appId) {
        WbPlatform.init(application, appId);
    }

    public interface ShareType {
        int WX_FAVORITE = 0;//微信收藏
        int WX_FRIEND = 1;//微信好友
        int WX_MOMENT = 2;//微信朋友圈
        int QQ_FRIEND = 3;//QQ好友
        int WI_BO = 4;//微博
    }

    public static void share(Activity activity, int shareType, ShareParams shareParam, ShareCallback callback) {
        HiShare.shareType = shareType;
        ISharePlatform sharePaltform = null;
        switch (shareType) {
            case ShareType.QQ_FRIEND:
                sharePaltform = new TencentPlatform();
                break;
            case ShareType.WX_FAVORITE:
            case ShareType.WX_FRIEND:
            case ShareType.WX_MOMENT:
                sharePaltform = new WxPlatform();
                break;
            case ShareType.WI_BO:
                sharePaltform = new WbPlatform();
                break;
        }
        if (sharePaltform != null) {
            sharePaltform.share(activity, shareType, shareParam, callback);
        }

    }

    public static class ShareParams {
        public ShareParams(String targetUrl, String title, String content, String imageUrl, Bitmap bitmap) {
            this.targetUrl = targetUrl;
            this.title = title;
            this.content = content;
            this.imageUrl = imageUrl;
            this.bitmap = bitmap;
        }

        public String targetUrl;
        public String title;
        public String content;
        public String imageUrl;
        public Bitmap bitmap;
    }

    /**
     * 用于qq分享的回调,如果分享的activity没有调用这个方法就会导致qq分享响应不到回调，如果没有调用QQ分享可以不用回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        TencentPlatform.onActivityResult(requestCode, resultCode, data);
    }
    public static void onNewIntent(Intent intent){
        WbPlatform.onNewIntent(intent);
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
        public void onCancel(int shareType) {
            Toast.makeText(application, "分享取消", Toast.LENGTH_SHORT).show();
        }
    }
}
