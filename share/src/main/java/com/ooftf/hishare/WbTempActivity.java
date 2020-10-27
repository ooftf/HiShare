package com.ooftf.hishare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ooftf.hishare.sina.WbPlatform;

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2020/10/27
 */
public class WbTempActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WbPlatform.current.shareReal(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        WbPlatform.current.onNewIntent(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        WbPlatform.current = null;
        super.onDestroy();
    }
}
