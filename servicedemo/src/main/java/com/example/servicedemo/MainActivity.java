package com.example.servicedemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button btn_start;
    private Button btn_stop;
    private Button btn_bind;
    private Button btn_unbind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start = findViewById(R.id.btn_start);
        btn_stop = findViewById(R.id.btn_stop);
        btn_bind = findViewById(R.id.btn_bind);
        btn_unbind = findViewById(R.id.btn_unbind);

        Intent intent = new Intent(this, MyService.class);


        btn_start.setOnClickListener(v -> {
            // Start service
            startService(intent);
        });

        btn_stop.setOnClickListener(v -> {
            // Stop service
            stopService(intent);
        });


        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i("MyService", "Service connected");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                //意外销毁
                Log.i("MyService", "Service disconnected");
            }
        };

        btn_bind.setOnClickListener(v -> {
            // Bind service

            bindService(intent,connection,BIND_AUTO_CREATE);
        });

        btn_unbind.setOnClickListener(v -> {
            // Unbind service
            unbindService(connection);
        });

    }
}