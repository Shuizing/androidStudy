package com.example.contentdemo.adpter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contentdemo.R;
import com.example.contentdemo.entity.Student;

import java.util.List;


public class StudentAdpter extends RecyclerView.Adapter<StudentAdpter.ViewHolder> {

    private List<Student> datas;

    public StudentAdpter(List<Student> datas) {
        this.datas = datas;
    }


    @NonNull
    @Override
    public StudentAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAdpter.ViewHolder holder, int position) {
        Student student = datas.get(position);
        holder.nameItem.setText(student.getName());
        holder.classmateItem.setText(student.getClassmate());
        holder.ageItem.setText(student.getAge());
        holder.itemView.setTag(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                   listener.onItemClick(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
    //item点击的回调接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameItem;
        TextView classmateItem;
        TextView ageItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameItem = itemView.findViewById(R.id.tv_name);
            classmateItem = itemView.findViewById(R.id.tv_classmate);
            ageItem = itemView.findViewById(R.id.tv_age);
        }
    }
}

