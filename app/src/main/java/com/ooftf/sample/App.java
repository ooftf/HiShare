package com.ooftf.sample;

import android.app.Application;

import com.ooftf.hishare.HiShare;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        HiShare.init(this);
        HiShare.initWXShare("微信appid");
        HiShare.initTencentShare("腾讯appid");
    }
}
