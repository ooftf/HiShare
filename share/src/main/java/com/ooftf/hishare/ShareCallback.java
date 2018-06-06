package com.ooftf.hishare;

public interface ShareCallback {
    void onError(int shareType, int code);

    void onSuccess(int shareType);

    void onCancel(int shareType);

    interface ErrorCode {
        int LOAD_IMAGE = 0;
        int AUTH_DENIED = 1;
        int UNSUPPORTED = 2;
        int UNKNOWN = 3;
        int WX_UNINSTALLED = 4;
        int QQ_UNINSTALLED = 5;
        int SAVE_BITMAP = 6;
    }
}
