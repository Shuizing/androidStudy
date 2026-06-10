package com.example.musicplayer.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.musicplayer.PlayerFragment;
import com.example.musicplayer.until.Media;
import com.example.musicplayer.until.MusicPlayer;

public class MyReceiver extends BroadcastReceiver {


    private PlayerFragment playerFragment;

    public MyReceiver(PlayerFragment playerFragment) {
        this.playerFragment = playerFragment;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("isPlaying")){
            boolean s = intent.getBooleanExtra("isPlaying", false);
            Log.i(Media.TAG, "接收到本地广播: " + s);
                this.playerFragment.setReceiveStatus(s);
        }

        if (action.equals("songTime")){
//            Log.d(Media.TAG, "收到广播: songTime = " + intent.getLongExtra("time", 0));
            long time = intent.getLongExtra("time", 0);
                this.playerFragment.setReceiveTime(formatDuration(time));
        }

        if (action.equals("songComplete")){
//            Log.d(Media.TAG, "切换下一首");
                this.playerFragment.playNext();
        }
    }

    public static String formatDuration(long millis) {
        if (millis <= 0) {
            return "00:00";
        }

        long totalSeconds = millis / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }
}