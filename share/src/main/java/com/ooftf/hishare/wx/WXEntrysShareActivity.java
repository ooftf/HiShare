package com.ooftf.hishare.wx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ooftf.hishare.HiShare;
import com.ooftf.hishare.ShareCallback;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

public class WXEntrysShareActivity extends Activity implements IWXAPIEventHandler {
    IWXAPI iwxapi;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iwxapi = WXPlatform.createWXAPI();
        try {
            iwxapi.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        iwxapi.handleIntent(intent, this);

    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK://发送成功
                if (WXPlatform.callback != null) {
                    WXPlatform.callback.onSuccess(HiShare.shareType);
                    WXPlatform.callback = null;
                }

                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://发送取消
                if (WXPlatform.callback != null) {
                    WXPlatform.callback.onCancel();
                    WXPlatform.callback = null;
                }
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://发送被拒绝
                if (WXPlatform.callback != null) {
                    WXPlatform.callback.onError(HiShare.shareType, ShareCallback.ErrorCode.AUTH_DENIED);
                    WXPlatform.callback = null;
                }
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT://不支持错误wx
                if (WXPlatform.callback != null) {
                    WXPlatform.callback.onError(HiShare.shareType,ShareCallback.ErrorCode.UNSUPPORTED);
                    WXPlatform.callback = null;
                }
                break;
            default://发送返回
                if (WXPlatform.callback != null) {
                    WXPlatform.callback.onError(HiShare.shareType,ShareCallback.ErrorCode.UNKNOWN);
                    WXPlatform.callback = null;
                }
                ;
                break;
        }
        finish();
    }

}