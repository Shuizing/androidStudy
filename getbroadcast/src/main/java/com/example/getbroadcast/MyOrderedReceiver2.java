package com.example.getbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyOrderedReceiver2 extends BroadcastReceiver {


    public static final String ORDERED_ACTION = "com.example.broadcast.ORDERED_ACTION";
    public static final String TAG = "BROADCASTDEMO";

    final private Context context;

    public MyOrderedReceiver2(Context context) {
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "第二个接收者");
        String action = intent.getAction();
        if (action.equals(ORDERED_ACTION)){
            String resultData = getResultData();
            if (resultData != null){
                Toast.makeText(context, "第二个接收内容: " + resultData, Toast.LENGTH_SHORT).show();
                ((MainActivity)context).setTv_receiver("第二个接收内容为" + resultData);
                setResultData(resultData + "22");
            }

        }
    }
}