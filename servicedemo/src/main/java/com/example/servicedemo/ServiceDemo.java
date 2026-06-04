package com.example.servicedemo;

import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ServiceDemo extends AppCompatActivity {
    public final static String TAG = "DownloadService";
    private Button btn_bind;
    private Button btn_unbind;
    private Button btn_download;
    private ProgressBar progressBar;
    private DownloadService downloadService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {

            downloadService = ((DownloadService.MyBinder) service).getService();
            downloadService.setOnProgressListener(new DownloadService.OnProgressListener() {
                @Override
                public void onProgress(int progress) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progress);
                            if (progress == 100) {
//                             startActivity(new Intent(downloadService, ServiceDemo.class));
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            });
        }

        @Override
        public void onServiceDisconnected(android.content.ComponentName name) {
            Toast.makeText(ServiceDemo.this, "意外中断", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: 我已创建");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_service_demo);

        btn_bind = findViewById(R.id.btn_bind);

        btn_unbind = findViewById(R.id.btn_unbind);

        btn_download = findViewById(R.id.btn_download);

        progressBar = findViewById(R.id.progress_bar);

        btn_bind.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ServiceDemo.this, DownloadService.class);
                bindService(intent, serviceConnection, BIND_AUTO_CREATE);
            }
        });

        btn_download.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (downloadService == null){
                    Toast.makeText(ServiceDemo.this, "请先绑定服务", Toast.LENGTH_SHORT).show();
                }else {
                    downloadService.startDownload();
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
         btn_unbind.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (downloadService == null){
                    Toast.makeText(ServiceDemo.this, "暂未绑定服务，无需解绑", Toast.LENGTH_SHORT).show();
                }else {
                    unbindService(serviceConnection);
                    downloadService = null;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart:我已启动 ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume:我已恢复 ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause:我已暂停 ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop:我已停止 ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy:我已销毁 ");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.i(TAG, "onRestart:我已重启 ");
    }



}