package com.example.broadcast;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


public class MainActivity extends AppCompatActivity {

    public static final String NORMAL_ACTION = "com.example.broadcast.NORMAL_ACTION";
    public static final String ORDERED_ACTION = "com.example.broadcast.ORDERED_ACTION";
    public static final String LOCAL_ACTION = "com.example.broadcast.LOCAL_ACTION";
    public static final String NORMAL_ACTION_KEY = "key";
    public static final String LOCAL_ACTION_KEY = "local";
    public MyLocalReceiver myLocalReceiver;

    private LocalBroadcastManager localBroadcastManager;

    private TextView tv_receive;

    public void SetReceiveText(String text) {
        tv_receive.setText(text);
    }

    private EditText et_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button btn_local = findViewById(R.id.btn_local);
        Button btn_normal = findViewById(R.id.btn_normal);
        Button btn_ordered = findViewById(R.id.btn_ordered);
        tv_receive = findViewById(R.id.tv_receive);
        et_content = findViewById(R.id.et_content);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);


        //全局广播  普通
        btn_normal.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(NORMAL_ACTION);
            intent.putExtra(NORMAL_ACTION_KEY, et_content.getText().toString());
            sendBroadcast(intent);
        });

        //全局广播  有序
        btn_ordered.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(ORDERED_ACTION);
            //intent  权限  最终接收器   处理器   结果码   传递数据   传递数据包
            sendOrderedBroadcast(intent,null,null,null,RESULT_OK,et_content.getText().toString(),null);
        });

        //本地广播
        btn_local.setOnClickListener(v -> {
            Log.i(MyLocalReceiver.TAG, "发送本地广播");
            Intent intent = new Intent(LOCAL_ACTION);
            intent.putExtra(LOCAL_ACTION_KEY, et_content.getText().toString());
            localBroadcastManager.sendBroadcast(intent);
        });


        //注册本地广播
        myLocalReceiver = new MyLocalReceiver(this);
        localBroadcastManager.registerReceiver(myLocalReceiver, new IntentFilter(LOCAL_ACTION));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        localBroadcastManager.unregisterReceiver(myLocalReceiver);
    }


}

