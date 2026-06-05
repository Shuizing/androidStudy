package com.example.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyLocalReceiver extends BroadcastReceiver {

    public static final String TAG = "BROADCASTDEMO";
    final private Context context;
    public static final String LOCAL_ACTION = "com.example.broadcast.LOCAL_ACTION";
    public static final String LOCAL_ACTION_KEY = "local";
    public MyLocalReceiver(Context context) {
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(LOCAL_ACTION)){
            String s = intent.getStringExtra(LOCAL_ACTION_KEY);
            Log.i(TAG, "接收到本地广播: " + s);
            if (s != null){
                Toast.makeText(context, "本地广播: " + s, Toast.LENGTH_SHORT).show();
                ((MainActivity)this.context).SetReceiveText("本地广播: " + s);
            }

        }
    }
}