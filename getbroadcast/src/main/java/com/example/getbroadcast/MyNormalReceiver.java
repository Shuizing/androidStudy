package com.example.getbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyNormalReceiver extends BroadcastReceiver {

    public static final String NORMAL_ACTION = "com.example.broadcast.NORMAL_ACTION";
    public static final String NORMAL_ACTION_KEY = "key";

    final private Context context;

    public MyNormalReceiver(Context context) {
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(NORMAL_ACTION)){
            String s = intent.getStringExtra(NORMAL_ACTION_KEY);
            if(s!=null){
                ((MainActivity)context).setTv_receiver(s);
            }
        }
    }
}