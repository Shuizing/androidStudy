package com.example.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class DownloadService extends Service {

    public final static String TAG = "DownloadService";
    private int iProgress;
    private Thread downloadThread;

    public interface OnProgressListener{
        void onProgress(int progress);
    }

    private OnProgressListener onProgressListener;

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    public DownloadService() {
    }


    //服务主体
    public void startDownload(){
        Log.i(TAG, "开始下载");

        iProgress = 0;

        downloadThread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (iProgress < 100) {
                    iProgress += 20;
                    Log.i(TAG, "下载进度: " + iProgress);
                    // 通过监听器将现有进度值传递出去
                    if (onProgressListener != null) {
                        onProgressListener.onProgress(iProgress);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Log.i(TAG, "线程异常: " + e.getMessage());
                        e.printStackTrace();
                    }

                }


            }
        });
        downloadThread.start();
    }

    //IBinder的子类，用于将当前的Service传递给Activity
    class MyBinder extends Binder {
        DownloadService getService() {
            return DownloadService.this;
        }
    }


    @Override
    public void onCreate() {
        Log.i(TAG, "Service已创建");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if (downloadThread != null){
            downloadThread.interrupt();
            Log.i(TAG, "downloadThread已中断");
        }

        Log.i(TAG, "Service已销毁");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Service已解绑");
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "Service已绑定");
       return new MyBinder();
//        return DownloadService.this;
    }

}