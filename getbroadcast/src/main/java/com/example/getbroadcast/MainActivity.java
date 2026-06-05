package com.example.getbroadcast;

import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "BROADCASTDEMO";
    public static final String NORMAL_ACTION = "com.example.broadcast.NORMAL_ACTION";
    public static final String ORDERED_ACTION = "com.example.broadcast.ORDERED_ACTION";


    public void setTv_receiver(String s) {
        this.tv_receiver.setText(s);
    }

    private TextView tv_receiver;
    private MyNormalReceiver myNormalReceiver;
    private MyOrderedReceiver1 myOrderedReceiver1;
    private MyOrderedReceiver2 myOrderedReceiver2;
    private MyOrderedReceiver3 myOrderedReceiver3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        tv_receiver = findViewById(R.id.tv_receiver);

        // 注册普通广播接收者
        myNormalReceiver = new MyNormalReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NORMAL_ACTION);
        registerReceiver(myNormalReceiver, intentFilter,RECEIVER_EXPORTED);

        // 注册有序广播接收者
        myOrderedReceiver1 = new MyOrderedReceiver1(this);
        IntentFilter intentFilter1 = new IntentFilter(ORDERED_ACTION);
        intentFilter1.setPriority(2000);
        registerReceiver(myOrderedReceiver1, intentFilter1,RECEIVER_EXPORTED);

        myOrderedReceiver2 = new MyOrderedReceiver2(this);
        IntentFilter intentFilter2 = new IntentFilter(ORDERED_ACTION);
        intentFilter2.setPriority(1000);
        registerReceiver(myOrderedReceiver2, intentFilter2,RECEIVER_EXPORTED);

        myOrderedReceiver3 = new MyOrderedReceiver3(this);
        IntentFilter intentFilter3 = new IntentFilter(ORDERED_ACTION);
        intentFilter3.setPriority(100);
        registerReceiver(myOrderedReceiver3, intentFilter3,RECEIVER_EXPORTED);


    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy:我已销毁 ");
        super.onDestroy();
        Log.i(TAG, "开始销毁广播接收者");
        unregisterReceiver(myNormalReceiver);
        unregisterReceiver(myOrderedReceiver1);
        unregisterReceiver(myOrderedReceiver2);
        unregisterReceiver(myOrderedReceiver3);
    }


}