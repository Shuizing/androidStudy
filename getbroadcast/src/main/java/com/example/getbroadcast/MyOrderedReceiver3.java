package com.example.getbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyOrderedReceiver3 extends BroadcastReceiver {


    public static final String ORDERED_ACTION = "com.example.broadcast.ORDERED_ACTION";
    public static final String TAG = "BROADCASTDEMO";

    final private Context context;

    public MyOrderedReceiver3(Context context) {
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "最终接收者");
        String action = intent.getAction();
        if (action.equals(ORDERED_ACTION)){
            String resultData = getResultData();
            if (resultData != null){
                Toast.makeText(context, "最终接收内容: " + resultData, Toast.LENGTH_SHORT).show();
                ((MainActivity)context).setTv_receiver("最终接收内容为" + resultData);
            }

        }
    }
}