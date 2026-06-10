package com.example.musicplayer;



import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.musicplayer.Broadcast.MyReceiver;
import com.example.musicplayer.entity.Song;
import com.example.musicplayer.until.Media;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    private MainActivity mainActivity;
    private View view;
    private ImageButton ibtn_play;
    private List<Song> songList;
    private TextView tv_title;
    private TextView tv_album;
    private ImageButton ibtn_favorite;
    private TextView tv_songTime;
    private ImageButton ibtn_repeat;
    private ImageButton ibtn_prev;
    private ImageButton ibtn_next;
    private ImageButton ibtn_shuffle;

    private int currentIndex = 0;

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int index) {
        this.currentIndex = index;
    }

    public PlayerFragment() {
        // Required empty public constructor
    }
    public PlayerFragment(List<Song> songList){
         this.songList = songList;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayerFragment newInstance(String param1, String param2) {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_player, container, false);
        mainActivity = (MainActivity) getActivity();

        //注册本地广播
        MyReceiver myLocalReceiver = new MyReceiver(this);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(myLocalReceiver, new IntentFilter("isPlaying"));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(myLocalReceiver, new IntentFilter("songTime"));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(myLocalReceiver, new IntentFilter("completeSong"));


        tv_title = view.findViewById(R.id.tv_song_title);
        tv_title.setText(songList.get(0).getTitle());

        tv_album = view.findViewById(R.id.tv_song_album);
        tv_album.setText(songList.get(0).getAlbum());

        ibtn_favorite = view.findViewById(R.id.iv_favorite);


        tv_songTime = view.findViewById(R.id.tv_songTime);

        tv_songTime.setText(songList.get(0).getTime());

        ibtn_play = view.findViewById(R.id.ibtn_play);

        ibtn_repeat = view.findViewById(R.id.btn_repeat);
        ibtn_prev = view.findViewById(R.id.btn_prev);
        ibtn_next = view.findViewById(R.id.btn_next);
        ibtn_shuffle = view.findViewById(R.id.btn_shuffle);

        //下一首
        ibtn_next.setOnClickListener(v -> playNext());
        //上一首
        ibtn_prev.setOnClickListener(v -> playPrev());


        ibtn_play.setOnClickListener(v -> {
            //启动service
            Intent intent = new Intent(getContext(), MusicService.class);
            intent.putExtra("song", songList.get(currentIndex));
            intent.putExtra("action", "play");
            Log.i(Media.TAG, "主界面音乐播放");
            getContext().startService(intent);
        });
        return view;
    }

    public void playNext() {
        currentIndex = (currentIndex + 1) % songList.size();
        //切歌
        switchSong();
    }

    public void switchSong() {
        Song song = songList.get(currentIndex);

        // 更新 UI
        tv_title.setText(song.getTitle());
        tv_album.setText(song.getAlbum());
        tv_songTime.setText(song.getTime());
        ibtn_play.setImageResource(R.drawable.selector_button_pause);  // 切歌直接播放

        // 通知 Service 播放
        Intent intent = new Intent(getContext(), MusicService.class);
        intent.putExtra("song", song);
        intent.putExtra("action", "play");
        getContext().startService(intent);
    }


    public void playPrev() {
        currentIndex = (currentIndex - 1 + songList.size()) % songList.size();
        switchSong();
    }

    public void setReceiveStatus(boolean status) {
        Log.i(Media.TAG, "变更主界面播放状态：" + status);
        if(status == true){
            ibtn_play.setImageResource(R.drawable.selector_button_pause);
        }else{
            ibtn_play.setImageResource(R.drawable.selector_id3_button_play);
        }

    }

    public void setReceiveTime(String time) {

        tv_songTime.setText(time);
//        Log.i(Media.TAG, "变更主界面播放时间：" + time);
    }
}