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
        iwxapi = WxPlatform.createWXAPI();
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
                if (WxPlatform.callback != null) {
                    WxPlatform.callback.onSuccess(HiShare.shareType);
                    WxPlatform.callback = null;
                }

                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://发送取消
                if (WxPlatform.callback != null) {
                    WxPlatform.callback.onCancel();
                    WxPlatform.callback = null;
                }
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://发送被拒绝
                if (WxPlatform.callback != null) {
                    WxPlatform.callback.onError(HiShare.shareType, ShareCallback.ErrorCode.AUTH_DENIED);
                    WxPlatform.callback = null;
                }
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT://不支持错误wx
                if (WxPlatform.callback != null) {
                    WxPlatform.callback.onError(HiShare.shareType,ShareCallback.ErrorCode.UNSUPPORTED);
                    WxPlatform.callback = null;
                }
                break;
            default://发送返回
                if (WxPlatform.callback != null) {
                    WxPlatform.callback.onError(HiShare.shareType,ShareCallback.ErrorCode.UNKNOWN);
                    WxPlatform.callback = null;
                }
                ;
                break;
        }
        finish();
    }

}