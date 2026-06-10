package com.example.musicplayer.entity;

public class Song {

    private int id;

    //歌名
    private String title;

    //专辑名
    private String album;
    //歌手名
    private String artist;

    //时长
    private long duration;

    //文件路径
    private String path;


    //是否收藏  N/Y
    private String collect;

    //时长
    private String time;

    //封面
    private String cover;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Song() {
    }

    public Song(int id, String title, String album, String collect, String time, String cover) {
        this.id = id;
        this.title = title;
        this.album = album;
        this.collect = collect;
        this.time = time;
        this.cover = cover;
    }
    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
