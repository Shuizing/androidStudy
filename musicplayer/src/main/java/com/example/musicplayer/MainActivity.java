package com.example.musicplayer;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.musicplayer.entity.Song;
import com.example.musicplayer.until.Media;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String ALBUM_TAG = "album";
    public static final String PLAYER_TAG = "player";


    private volatile List<Song> songList;

    private PlayerFragment playerFragment;
    private AlbumListFragment albumListFragment;

    private NavigationBarFragment navigationBarFragment;

    private DrawerLayout drawerLayout;

    private View mainContent;

    // 添加公共方法供Fragment调用
    public void openDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    }


    public void closeDrawer() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
    }

    //侧边栏位置
    private float lastOffset = 1f;

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         drawerLayout = findViewById(R.id.drawer_layout);
         mainContent = findViewById(R.id.main);

        // 初始化 Fragment
        initFragments();


        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                mainContent.setTranslationX(drawerView.getWidth() * slideOffset);

                if (slideOffset < lastOffset) {
                    //  关闭
                    albumListFragment.getBtn_back().setImageResource(R.drawable.selector_arrows);
                } else if (slideOffset > lastOffset) {
                    //  打开
                }
                lastOffset = slideOffset;  // 更新记录
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mainContent.setTranslationX(0);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }


    // 初始化所有 Fragment 添加到容器中，默认playerfragment
    private void initFragments() {

        //初始化音乐列表
        Media media = new Media();
        songList = new ArrayList<>();
        // 动态申请权限
        // 先检查权限，再加载歌曲
        if (checkAudioPermission()) {
            songList = media.scanMusic(this.getContentResolver());
            if (songList.isEmpty()) {
                songList = media.scanMusicByFile();
            }
        } else {
            requestAudioPermission();
        }


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        // 创建或复用Fragment实例
        playerFragment = (PlayerFragment) fm.findFragmentByTag("PlayerFragment");
        albumListFragment = (AlbumListFragment) fm.findFragmentByTag("AlbumListFragment");
        // 首次创建
        if (playerFragment == null) {
            playerFragment = new PlayerFragment(songList);
        }
        if (albumListFragment == null) {
            albumListFragment = new AlbumListFragment(songList);
        }

         navigationBarFragment = new NavigationBarFragment();

        // Fragment添加容器
        // 通过 show/hide 切换，避免重复 create/destroy
        transaction.add(R.id.fragmentContainer, playerFragment, "PlayerFragment");
        transaction.add(R.id.fragmentContainer, albumListFragment, "AlbumListFragment");
        transaction.add(R.id.navigationBarFragment, navigationBarFragment, "NavigationBarFragment");

        // 隐藏 AlbumListFragment，只显示 PlayerFragment
        // playerFragment 默认就是 show 状态（add 后自动 show）
        transaction.hide(albumListFragment);

        transaction.commit();

        // 记录当前show的 Fragment
        currentFragment = playerFragment;
    }

    //切换fragment,传递选中的歌
    public void switchFragment(String target) {
        // 判断当前Fragment是否存在
        if (currentFragment == null) return;

        // 如果目标就是当前正在显示的，直接跳过
        if (PLAYER_TAG.equals(target) && currentFragment instanceof PlayerFragment) return;
        if (ALBUM_TAG.equals(target) && currentFragment instanceof AlbumListFragment) return;

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();


        // 隐藏当前 Fragment
        transaction.hide(currentFragment);

        // 显示目标 Fragment
        if (PLAYER_TAG.equals(target)) {
            transaction.show(playerFragment);
            currentFragment = playerFragment;
        } else if (ALBUM_TAG.equals(target)) {
            albumListFragment.setSelectedIndex(playerFragment.getCurrentIndex());
            transaction.show(albumListFragment);
            currentFragment = albumListFragment;


        }


        transaction.commit();

    }

    /**
     * 播放指定位置的歌曲，并同步两个 Fragment 的状态
     */
    public void playSongAt(int position) {
        // 1. 更新 PlayerFragment 的索引和 UI
        playerFragment.setCurrentIndex(position);
        playerFragment.switchSong();

        // 2. 同步到列表
        albumListFragment.setSelectedIndex(position);
    }

    /**
     * 获取当前播放索引（供 AlbumListFragment 切入时读取）
     */
    public int getCurrentPlayingIndex() {
        return playerFragment.getCurrentIndex();
    }

    private static final int REQUEST_AUDIO_PERMISSION = 1001;

    private boolean checkAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+
            return ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED;
        } else {
            // Android 12 及以下
            return ContextCompat.checkSelfPermission(this,
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


}