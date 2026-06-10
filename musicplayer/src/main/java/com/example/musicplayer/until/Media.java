package com.example.musicplayer.until;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.musicplayer.entity.Song;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Media {

    public static final String TAG = "MediaUtil";



    /**
     * 扫描 Music 文件夹下的所有歌曲
     * @param contentResolver ContentResolver 对象
     * @return 歌曲信息列表
     */
    public static List<Song> scanMusic(ContentResolver contentResolver) {
        List<Song> songList = new ArrayList<>();

        String[] projection = {
                MediaStore.Audio.Media.TITLE,      // 标题
                MediaStore.Audio.Media.ARTIST,     // 艺术家
                MediaStore.Audio.Media.ALBUM,      // 唱片集
                MediaStore.Audio.Media.DURATION,   // 时长（毫秒）
                MediaStore.Audio.Media.DATA        // 文件路径
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null
        );

        if (cursor != null) {
            Log.d(TAG, "cursor count: " + cursor.getCount());  // 打印总数

            int titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int durationIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int pathIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            while (cursor.moveToNext()) {
                String path = cursor.getString(pathIndex);

//                 只取 Music 文件夹下的文件
                if (path != null && path.startsWith("/storage/emulated/0/Music/")) {
                    String title = cursor.getString(titleIndex);
                    String artist = cursor.getString(artistIndex);
                    String album = cursor.getString(albumIndex);
                    long duration = cursor.getLong(durationIndex);

                    Song song = new Song();
                    song.setTitle(title);
                    song.setArtist(artist);
                    song.setAlbum(album);
                    song.setDuration(duration);
                    song.setPath(path);
                    song.setCollect("Y");
                    songList.add(song);
                }
            }
            cursor.close();
        }else {
            Log.d(TAG, "cursor is null");
        }

        return songList;
    }

    public List<Song> scanMusicByFile() {
        List<Song> songList = new ArrayList<>();
        File musicDir = new File("/storage/emulated/0/Music/");

        if (!musicDir.exists() || !musicDir.isDirectory()) {
            return songList;
        }

        Log.i(TAG, "文件存在");
        File[] files = musicDir.listFiles();
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                // 只处理音频文件
                if (fileName.endsWith(".mp3") || fileName.endsWith(".flac") || fileName.endsWith(".ogg")) {
                    Song song = new Song();
                    song.setPath(file.getAbsolutePath());

                    // 从文件名提取标题（去掉扩展名）
                    String title = fileName.substring(0, fileName.lastIndexOf("."));
                    song.setTitle(title);

                    // 从 MediaMetadataRetriever 获取更详细的 ID3 信息（可选）
                    // 这里先简单设置，后续可以优化
                    song.setArtist("未知歌手");
                    song.setAlbum("未知专辑");
                    song.setCollect("Y");

                    songList.add(song);
                }
            }
        }
        return songList;
    }

}
