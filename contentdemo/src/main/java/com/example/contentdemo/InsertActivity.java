package com.example.contentdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.contentdemo.Dao.StudentDao;
import com.example.contentdemo.entity.Student;

public class InsertActivity extends AppCompatActivity {

    private EditText ed_name, ed_age;
    private Button btn_c, btn_q;
    private Spinner spinner;
    private Student currentStudent;
    private StudentDao studentDao = new StudentDao(this);
    private boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        initView();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            currentStudent = (Student) bundle.get("student");
        }
        //控件加载控件
        if(currentStudent != null){
            isUpdate = true;
            ed_name.setText(currentStudent.getName());
            ed_age.setText(currentStudent.getAge());
            SpinnerAdapter spinnerAdapter = spinner.getAdapter();
            for(int i=0; i < spinnerAdapter.getCount(); i++){
                if(spinnerAdapter.getItem(i).toString()
                        .equals(currentStudent.getClassmate())){
                    spinner.setSelection(i);
                    break;
                }
            }
        }



    }

    private void initView() {
        ed_age = findViewById(R.id.ed_age);
        ed_name = findViewById(R.id.ed_name);
        spinner = findViewById(R.id.spinner);
        btn_c = findViewById(R.id.button_c);
        btn_q = findViewById(R.id.button_q);

        btn_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Student student = new Student(ed_name.getText().toString(),
                        spinner.getSelectedItem().toString(),
                        Integer.parseInt(ed_age.getText().toString()));
                if (isUpdate) {
                    student.set_id(currentStudent.get_id());
                    studentDao.update(student);
                }else {
                    studentDao.insert(student);
                }
                //返回activity,刷新RecyclerView
                setResult(RESULT_OK,new Intent());
                finish();
            }
        });

        btn_q.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }
}