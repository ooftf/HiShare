package com.ooftf.hishare.sina;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.ShareCompat;

import com.ooftf.hishare.HiShare;
import com.ooftf.hishare.ISharePlatform;
import com.ooftf.hishare.ShareCallback;

public class WbPlatform implements ISharePlatform {
    private static Application application;
    private static String appId;
    public static ShareCallback callback;
    public static void init(Application application, String appId){
        WbPlatform.appId =appId;
        WbPlatform.application =application;
    }
    @Override
    public void share(Activity activity, int shareType, HiShare.ShareParams shareParam, ShareCallback callback) {
        shareReal(shareParam, callback);
    }

    private void shareReal(HiShare.ShareParams shareParam, ShareCallback callback) {
        WbPlatform.callback = callback;
        WbShareActivity.getStartIntent(application,shareParam.targetUrl,shareParam.title,shareParam.content,shareParam.imageUrl,shareParam.bitmap);
    }

}
