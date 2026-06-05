package com.example.contentdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contentdemo.Dao.StudentDao;
import com.example.contentdemo.adpter.StudentAdpter;
import com.example.contentdemo.entity.Student;

import java.io.Serializable;
import java.util.List;

public class StudentActivity extends AppCompatActivity {

    private List<Student> datas;
    private StudentDao dao;
    private Student currentStudent;
    private StudentAdpter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        //获取数据库
        dao = new StudentDao( this);
        //获取数据集
        datas = dao.selectAll();

        //组件初始化
        initView();
    }

    private void initView() {
        Button btn_add = findViewById(R.id.btn_add);
        Button btn_update = findViewById(R.id.btn_update);
        Button btn_delete = findViewById(R.id.btn_delete);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentActivity.this, InsertActivity.class);
                startActivityForResult(intent, 100); //跳转请求代码为100表示 增加记录
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //选中的记录信息传递去修改页面
                Bundle bundle = new Bundle();
                bundle.putSerializable("student", (Serializable) currentStudent);
                Intent intent = new Intent(StudentActivity.this, InsertActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 101); //跳转请求代码为101表示 修改记录
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StudentActivity.this).setTitle("删除")
                        .setMessage("确认删除？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dao.delete(currentStudent.get_id());
                                changeData();
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {

                            }
                        });
                builder.create().show();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new StudentAdpter(datas);
        adapter.setOnItemClickListener(new StudentAdpter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                adapter.setSelectedIndex(position);
                currentStudent=datas.get(position);
                Toast.makeText(StudentActivity.this,"第"+(position+1)+"个",Toast.LENGTH_SHORT).show();
            }

        });

        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
           super.onActivityResult(requestCode, resultCode, data);
           if ((requestCode == 100)||(requestCode == 101)&& resultCode == RESULT_OK){
               changeData();
               adapter.notifyDataSetChanged();
           }
    }

    private void changeData() {
        datas.clear();
        datas.addAll(dao.selectAll());
    }
}