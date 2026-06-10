package com.example.musicplayer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.entity.Song;
import com.example.musicplayer.until.Media;
import com.example.musicplayer.until.MusicPlayer;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private List<Song> datas;

    public SongAdapter(List<Song> datas) {
        this.datas = datas;
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {

        ProgressBar pb_play;
        ImageView iv_playIcon;
         TextView tv_songTitle;
         ImageButton btn_favorite;


        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_playIcon = itemView.findViewById(R.id.iv_play_icon);
            tv_songTitle = itemView.findViewById(R.id.tv_song_name);
            btn_favorite = itemView.findViewById(R.id.btn_favorite);
            pb_play = itemView.findViewById(R.id.pb_playing);

        }
    }



    @Override
    public void onBindViewHolder(@NonNull SongAdapter.SongViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Song song = datas.get(position);
        holder.tv_songTitle.setText(song.getTitle());
        Context context = holder.itemView.getContext();

        if(song.getCollect().equals("Y")){
            holder.btn_favorite.setImageResource(R.drawable.list_icon_collect_1);
        }else {
            holder.btn_favorite.setImageResource(R.drawable.list_icon_collect_2);
        }

        // 为收藏按钮设置点击事件
        holder.btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(view, position);
                }
                if (song.getCollect().equals("Y")) {
                    song.setCollect("N");
                    holder.btn_favorite.setImageResource(R.drawable.list_icon_collect_2);
                } else {
                    song.setCollect("Y");
                    holder.btn_favorite.setImageResource(R.drawable.list_icon_collect_1);
                }
                //写入数据库 现在没数据库
            }
        });

        // 根据是否选中来设置不同的样式
        if (position == selectedIndex) {
            // 设置字体颜色
            holder.tv_songTitle.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.tv_songTitle.setTextSize(26);
            holder.iv_playIcon.setImageResource(R.drawable.list_icon_play);
            //播放音乐
            MusicPlayer.getInstance().play(song.getPath());
            Log.i(Media.TAG, "开始播放音乐，路径： " + song.getPath());
            //设置进度条
            holder.pb_play.setVisibility(View.VISIBLE);


        }else {
            // 未选中状态 重置
            holder.tv_songTitle.setTextColor(ContextCompat.getColor(context, R.color.album_default_textColor));
            holder.tv_songTitle.setTextSize(21); // 恢复默认字号
            holder.iv_playIcon.setImageResource(R.drawable.list_icon_music);
            holder.pb_play.setVisibility(View.GONE);

        }

        // 点击整行时更新选中状态
        holder.itemView.setOnClickListener(v -> {
            int oldSelected = selectedIndex;
            selectedIndex = position;

            // 刷新旧的选中项和新的选中项，效率更高
            if (oldSelected != -1) {
                notifyItemChanged(oldSelected);
            }
            notifyItemChanged(position);
        });

    }

    @NonNull
    @Override
    public SongAdapter.SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }



    //点击事件
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    private int selectedIndex = -1;


    // 提供给外部调用的方法，用于更新选中项
    public void setSelectedIndex(int position) {
        int oldSelected = selectedIndex;
        selectedIndex = position;
        if (oldSelected != -1) {
            notifyItemChanged(oldSelected);
        }
        notifyItemChanged(position);
    }

    // 获取当前选中的歌曲
    public Song getSelectedSong() {
        if (selectedIndex != -1 && selectedIndex < datas.size()) {
            return datas.get(selectedIndex);
        }
        return null;
    }

}
