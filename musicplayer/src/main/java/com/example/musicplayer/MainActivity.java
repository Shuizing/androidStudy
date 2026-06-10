package com.example.musicplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String ALBUM_TAG = "album";
    public static final String PLAYER_TAG = "player";


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
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        // 创建或复用Fragment实例
        playerFragment = (PlayerFragment) fm.findFragmentByTag("PlayerFragment");
        albumListFragment = (AlbumListFragment) fm.findFragmentByTag("AlbumListFragment");
        // 首次创建
        if (playerFragment == null) {
            playerFragment = new PlayerFragment();
        }
        if (albumListFragment == null) {
            albumListFragment = new AlbumListFragment();
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
        // commit() 是异步的，commitNow() 才是同步立即执行
        transaction.commit();

        // 记录当前show的 Fragment
        currentFragment = playerFragment;
    }

    //切换fragment
    public void switchFragment(String target) {
        // 判断当前Fragment是否存在
        if (currentFragment == null) return;

        // 如果目标就是当前正在显示的，直接跳过，不做无用功
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
            transaction.show(albumListFragment);
            currentFragment = albumListFragment;
        }


        transaction.commit();

    }



}