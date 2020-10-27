package com.ooftf.hishare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ooftf.hishare.tencent.TencentPlatform;

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2020/10/27
 */
public class QQTempActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TencentPlatform.current.share(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TencentPlatform.current.onActivityResult(requestCode, resultCode, data);
        finish();
    }

    @Override
    protected void onDestroy() {
        TencentPlatform.current = null;
        super.onDestroy();
    }
}
