package com.example.musicplayer.until;

import android.media.MediaPlayer;
import android.widget.Toast;

import java.io.IOException;

public class MusicPlayer {

    private static MusicPlayer instance;
    private MediaPlayer mediaPlayer;
    private String currentPath;

    // 私有构造函数
    private MusicPlayer() {
        mediaPlayer = new MediaPlayer();
    }

    // 全局访问点
    public static synchronized MusicPlayer getInstance() {
        if (instance == null) {
            instance = new MusicPlayer();
        }
        return instance;
    }
    /**
     * 播放音乐
     * @param path 文件路径
     */
    public void play(String path) {
        if (path.equals(currentPath) && mediaPlayer.isPlaying()) {
            return;
        }

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            currentPath = path;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 暂停
     */
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    /**
     * 恢复播放
     */
    public void resume() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    /**
     * 停止
     */
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * 是否正在播放
     */
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    /**
     * 获取当前播放路径
     */
    public String getCurrentPath() {
        return currentPath;
    }

}
