package com.ooftf.hishare;

import android.widget.Toast;

public class DefaultShareCallback implements ShareCallback {
    @Override
    public void onError(int shareType, int code) {
        switch (code) {
            case ErrorCode.WX_UNINSTALLED:
                Toast.makeText(HiShare.application, "没有检测到微信应用，请安装后再试", Toast.LENGTH_SHORT).show();
                break;
            case ErrorCode.QQ_UNINSTALLED:
                Toast.makeText(HiShare.application, "没有检测到QQ应用，请安装后再试", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(HiShare.application, "分享失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccess(int shareType) {
        Toast.makeText(HiShare.application, "分享成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(int shareType) {
        Toast.makeText(HiShare.application, "分享取消", Toast.LENGTH_SHORT).show();
    }
}
