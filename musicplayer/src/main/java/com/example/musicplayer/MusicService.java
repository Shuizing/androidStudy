package com.example.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.musicplayer.entity.Song;
import com.example.musicplayer.until.Media;
import com.example.musicplayer.until.MusicPlayer;

public class MusicService extends Service {

    //播放进度
    private int progress;

    //是否在播放
    private boolean isPlaying;
    private Thread progressThread;
    private boolean isRunning;
    private String currentPath = null;

    private long remainingDuration;  // 剩余时长（毫秒）
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable progressRunnable;
    private static final long INTERVAL = 250;
    public MusicService(){
    }


    //播放音乐
    public void playMusic(Song song) {
        boolean isPlaying = MusicPlayer.getInstance().play(song.getPath());
        sendPlaybackStatus(isPlaying, song.getPath());
//        startProgressUpdate(song);

        // 新歌 or 切歌
        if (currentPath == null || !currentPath.equals(song.getPath())) {
            currentPath = song.getPath();
            remainingDuration = song.getDuration();
            isRunning = true;
            startProgress();  // 启动进度轮
        } else {
            // 同一首歌：暂停/继续
            if (isRunning) {
                pauseProgress();  // 暂停
            } else {
                resumeProgress(); // 继续
            }
        }
    }

//    private void startProgressUpdate(Song song) {
//        Log.i(Media.TAG, "开始更新进度");
//        final long interval = 250; // 250毫秒
//        long duration = song.getDuration();
//
//        final Handler handler = new Handler(Looper.getMainLooper());
//        Log.i(Media.TAG, "currentPath " + currentPath);
//
//        //第一首
//        if (currentPath == null){
//            isRunning = true;
//            Log.i(Media.TAG, "第一首");
//            currentPath = song.getPath();
//            progressThread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    Log.i(Media.TAG, "更新进度");
//                    long currentDuration = duration;
//
//                    while (isRunning  &&   currentDuration > 0) {
//                        try {
//                            Thread.sleep(interval);
//                            currentDuration = currentDuration - interval;
//                            sendProgress(currentDuration);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                            break;
//                        }
//                    }
//                }
//            });
//            progressThread.start();
//        }else {
//            //暂停或继续
//            if (currentPath.equals(song.getPath())){
//                //判断线程状态
//                //运行  暂停
//                if (isRunning){
//
//                    Log.i(Media.TAG, "暂停");
//                    isRunning = false;
//                }else {
//                    //暂停  继续
//                    Log.i(Media.TAG,"运行");
//                    isRunning = true;
//                }
//            }
//        }
//
//
//        //切歌
//
//
//    }



    private void startProgress() {
        // 防重复
        stopProgress();
        isRunning = true;
        progressRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isRunning) return;

                // 扣减时间
                remainingDuration -= INTERVAL;

                if (remainingDuration <= 0) {
                    remainingDuration = 0;
                    sendProgress(0);
                    // 这里可以触发下一首
                    stopProgress();

                    // 通知 Fragment 播放下一首
                    Intent intent = new Intent("songComplete");
                    LocalBroadcastManager.getInstance(MusicService.this)
                            .sendBroadcast(intent);

                    return;
                }

                sendProgress(remainingDuration);
                // 继续调度下一次
                handler.postDelayed(this, INTERVAL);
            }
        };
        handler.post(progressRunnable);
    }

    private void pauseProgress() {
        isRunning = false;
        // 取消队列中等待的 Runnable（不杀死，只是不执行）
        if (progressRunnable != null) {
            handler.removeCallbacks(progressRunnable);
        }
        Log.i(Media.TAG, "进度暂停在: " + remainingDuration);
    }

    private void resumeProgress() {
        if (remainingDuration <= 0) return;
        isRunning = true;
        // Runnable 本身还在，重新投递即可继续
        handler.postDelayed(progressRunnable, INTERVAL);
        Log.i(Media.TAG, "进度从 " + remainingDuration + " 恢复");
    }

    private void stopProgress() {
        isRunning = false;
        if (progressRunnable != null) {
            handler.removeCallbacks(progressRunnable);
        }
    }

    private void sendPlaybackStatus(boolean isPlaying, String currentPath) {
        Intent intent = new Intent("isPlaying");
        intent.putExtra("isPlaying", isPlaying);
        intent.putExtra("currentPath", currentPath);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        Log.d(Media.TAG, "发送广播: isPlaying = " + isPlaying);
    }

    private void sendProgress(long progress) {
        Log.i(Media.TAG, "发送进度");
        Intent intent = new Intent("songTime");
        intent.putExtra("time", progress);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }



    //暂停音乐
    public void stopMusic(){
        MusicPlayer.getInstance().stop();
    }

    private void pauseMusic() {
        MusicPlayer.getInstance().pause();
    }




    class MyBinder extends Binder {
        MusicService getService() { return MusicService.this; }
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        //停止播放
//        MusicPlayer.getInstance().release();
        stopProgress();
        super.onDestroy();
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 处理从传来的数据
        Log.i(Media.TAG, "service intent: " + intent);
        if (intent != null) {
            String action = intent.getStringExtra("action");
            Song song = (Song) intent.getSerializableExtra("song");
            Log.i(Media.TAG, "service动作为" + action);
            Log.i(Media.TAG, "service播放的歌曲为" + song.getTitle());

            switch (action) {
                case "play":
                    playMusic(song);
                    break;
                case "pause":
                    pauseMusic();
                    break;

            }
        }
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }
}
