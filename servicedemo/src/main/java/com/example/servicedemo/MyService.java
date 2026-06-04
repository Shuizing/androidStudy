package com.example.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    public MyService() {
    }

    static String tag = "MyService";

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(tag, "Service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(tag, "Service started");
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.i(tag, "Service bound");
       return null;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(tag, "Service unbound");
        return super.onUnbind(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(tag, "Service destroyed");
    }


}