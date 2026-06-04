package com.example.contentdemo.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.contentdemo.entity.Student;
import com.example.contentdemo.utils.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class StudentDao {

    private DBHelper dbHelper;
    public StudentDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    // 插入一条数据
    public void insert(String name, String classmate, int age) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("classmate", classmate);
        values.put("age", age);
        database.insert("t_student", null, values);
        database.close();
    }

    // 修改数据
    public void update(String name, String classmate, int age) {
        // 通过dbHelper对象获取数据库对象
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        // 封装数据
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("classmate", classmate);
        values.put("age", age);
        database.update("t_student", values, "_id=?", new String[]{"1"});
        database.close();
    }

    // 删除一条数据
    public void delete(int _id) {
        // 通过 dbHelper 对象获取数据库对象
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete("t_student", "_id=?", new String[]{String.valueOf(_id)});
        database.close();
    }

    // 查询所以数据
    public List<Student> selectAll() {
        List<Student> students = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query("t_student", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Student student = new Student(
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("classmate")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("age"))
            );
            student.set_id(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
            students.add(student);
        }
        cursor.close();
        database.close();
        return students;
    }












}
