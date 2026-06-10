package com.example.musicplayer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.musicplayer.adapter.SongAdapter;
import com.example.musicplayer.entity.Song;
import com.example.musicplayer.until.Media;

import java.util.ArrayList;
import java.util.List;


public class AlbumListFragment extends Fragment {



    // 参数键名，用于在 Bundle 中标识传递的参数
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // 接收外部传入的参数值
    private String mParam1;
    private String mParam2;
    private Context context;
    private View root;

    public ImageButton getBtn_back() {
        return btn_back;
    }

    public void setBtn_back(ImageButton btn_back) {
        this.btn_back = btn_back;
    }

    private ImageButton btn_back;

    //必须的空构造函数,Fragment 被系统重建时会调用此构造函数，不能删除
    public AlbumListFragment() {}
    public AlbumListFragment(Context context) {
        this.context = context;
    }

    /**
     * 工厂方法：创建 Fragment 实例并传入参数
     * 外部应通过此方法创建 Fragment，而不是直接 new
     *
     * @param param1 参数1
     * @param param2 参数2
     * @return 带有参数的 Fragment 实例
     */
    public static AlbumListFragment newInstance(String param1, String param2) {
        AlbumListFragment fragment = new AlbumListFragment();
        // 创建 Bundle 用于存放参数
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        // 将参数绑定到 Fragment
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Fragment 创建时调用
     * 在此处获取传入的参数
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 从 Bundle 中取出参数
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * 创建并返回 Fragment 的视图（界面）
     * 这是 Fragment 最核心的方法之一
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 加载布局文件，生成 View 对象
        root = inflater.inflate(R.layout.fragment_album_list, container, false);
        btn_back = root.findViewById(R.id.btn_back);

        btn_back.setOnClickListener(v -> {
            btn_back.setImageResource(R.drawable.selector_topbar_arrows2);
            // 获取 Activity 中的 DrawerLayout
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openDrawer();
            }

        });


        // 先检查权限，再加载歌曲
        if (checkAudioPermission()) {
            loadSongs();
        } else {
            requestAudioPermission();
        }

        RecyclerView recyclerView = root.findViewById(R.id.rv_song_list);

        // 创建数据列表
        Media media = new Media();
        List<Song> songList = media.scanMusic(getActivity().getContentResolver());
        songList.clear();
        if(songList.size() == 0){
            Log.i(Media.TAG, "文件扫描歌曲");
            songList = media.scanMusicByFile();
        }


        SongAdapter adapter = new SongAdapter(songList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        return root;
    }


    private static final int REQUEST_AUDIO_PERMISSION = 1001;

    private boolean checkAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+
            return ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED;
        } else {
            // Android 12 及以下
            return ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(new String[]{Manifest.permission.READ_MEDIA_AUDIO},
                    REQUEST_AUDIO_PERMISSION);
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_AUDIO_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限授予，加载歌曲
                loadSongs();
            } else {
                // 权限拒绝，提示用户
                Toast.makeText(requireContext(), "需要音频权限才能扫描歌曲", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void loadSongs() {
        RecyclerView recyclerView = root.findViewById(R.id.rv_song_list);
        Media media = new Media();
        List<Song> songList = media.scanMusic(requireActivity().getContentResolver());
        if (songList.isEmpty()) {
            songList = media.scanMusicByFile();
        }
        SongAdapter adapter = new SongAdapter(songList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }
}