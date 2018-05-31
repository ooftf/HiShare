package com.ooftf.hishare;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Environment;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

class BitmapUtils {

    @SuppressLint("CheckResult")
    static Observable<Bitmap> loadImage(final String url) {
        return Observable.just(url)
                .flatMap(new Function<String, ObservableSource<Bitmap>>() {
                    @Override
                    public ObservableSource<Bitmap> apply(String s) throws Exception {
                        return Observable.just(HiShare.urlToBitmap.convert(url));
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 将Bitmap文件保存到本地，并返回path
     *
     * @param bitmap
     * @return
     */
    static Observable<String> pathFromBitmap(Bitmap bitmap) {
        return Observable.just(bitmap).flatMap(new Function<Bitmap, ObservableSource<String>>() {

            @Override
            public ObservableSource<String> apply(Bitmap bitmap) throws Exception {
                File file = getBitmapFile();
                saveBitmap(bitmap, file, Bitmap.CompressFormat.JPEG, true);
                return Observable.just(file.getPath());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 保存图片
     *
     * @param src
     * @param file
     * @param format
     * @param recycle
     * @return
     */
    private static boolean saveBitmap(final Bitmap src,
                                      final File file,
                                      final Bitmap.CompressFormat format,
                                      final boolean recycle) throws IOException {
        OutputStream os = null;
        boolean ret = false;

        os = new BufferedOutputStream(new FileOutputStream(file));
        ret = src.compress(format, 100, os);
        if (recycle && !src.isRecycled()) src.recycle();
        if (os != null) {
            os.close();
        }
        return ret;
    }

    /**
     * 获取保存图片的路径
     *
     * @return
     */
    static File getBitmapFile() {
        File rootFile;
        if (isHaveSDCard()) {
            rootFile = Environment.getExternalStorageDirectory();
        } else {
            rootFile = Environment.getDataDirectory();
        }
        File parent = new File(rootFile.getPath() + File.separator + "share" + File.separator);
        if (!parent.exists()) parent.mkdirs();
        File targetFile = new File(parent.getPath() + "share.jpg");
        if (targetFile.exists()) targetFile.delete();
        return targetFile;
    }

    /**
     * 是否有sd卡
     *
     * @return
     */
    public static boolean isHaveSDCard() {
        String SDState = android.os.Environment.getExternalStorageState();
        if (SDState.equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
}
