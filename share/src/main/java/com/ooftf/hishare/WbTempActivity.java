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
        WbPlatform.current.shareReal();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        WbPlatform.current.onNewIntent(data);
        finish();
    }

    @Override
    protected void onDestroy() {
        WbPlatform.current = null;
        super.onDestroy();
    }
}
