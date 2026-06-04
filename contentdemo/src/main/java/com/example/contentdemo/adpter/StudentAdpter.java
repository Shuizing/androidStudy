package com.example.contentdemo.adpter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contentdemo.R;


public class StudentAdpter extends RecyclerView.Adapter<StudentAdpter.ViewHolder> {

    @NonNull
    @Override
    public StudentAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);
//        ViewHolder viewHolder = new ViewHolder(view);
//        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAdpter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameItem;
        TextView classmateItem;
        TextView ageItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            nameItem = itemView.findViewById(R.id.tv_name);
//            classmateItem = itemView.findViewById(R.id.tv_classmate);
            // 图片截断处推测为: ageItem = itemView.findViewById(R.id.tv_age);
        }
    }
}

