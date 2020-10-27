package com.ooftf.hishare;

import android.app.Activity;

public interface ISharePlatform {

    void share(int shareType, HiShare.ShareParams shareParam, ShareCallback callback);
}
