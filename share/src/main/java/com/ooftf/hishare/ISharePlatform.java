package com.ooftf.hishare;

import android.app.Activity;

public interface ISharePlatform {

    void share(Activity activity, int shareType, HiShare.ShareParams shareParam, ShareCallback callback);
}
