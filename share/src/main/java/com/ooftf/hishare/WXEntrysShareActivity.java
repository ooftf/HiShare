package com.ooftf.hishare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

public class WXEntrysShareActivity extends Activity implements IWXAPIEventHandler {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            WXShare.getWxapiInstance().handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        WXShare.getWxapiInstance().handleIntent(intent, this);

    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK://发送成功
                if (WXShare.callback != null) {
                    WXShare.callback.onSuccess(HiShare.shareType);
                    WXShare.callback = null;
                }

                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://发送取消
                if (WXShare.callback != null) {
                    WXShare.callback.onCancel();
                    WXShare.callback = null;
                }
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://发送被拒绝
                if (WXShare.callback != null) {
                    WXShare.callback.onError(HiShare.shareType,ShareCallback.ErrorCode.AUTH_DENIED);
                    WXShare.callback = null;
                }
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT://不支持错误wx
                if (WXShare.callback != null) {
                    WXShare.callback.onError(HiShare.shareType,ShareCallback.ErrorCode.UNSUPPORTED);
                    WXShare.callback = null;
                }
                break;
            default://发送返回
                if (WXShare.callback != null) {
                    WXShare.callback.onError(HiShare.shareType,ShareCallback.ErrorCode.UNKNOWN);
                    WXShare.callback = null;
                }
                ;
                break;
        }
        finish();
    }

}