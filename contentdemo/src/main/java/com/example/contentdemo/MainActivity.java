package com.example.contentdemo;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "MainActivity";

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> contacts = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.lv_contact);

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,contacts);
        listView.setAdapter(adapter);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }else{
            Log.i(TAG, "路径一，开始读取联系人");
            readContacts();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "路径二，开始读取联系人");
                    readContacts();
                }else {
                    Toast.makeText(this, "联系人获取权限失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private void readContacts() {
        if(contacts.size() > 0){
            contacts.clear();
        }
        Cursor cursor = null;

        try{
            cursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  // 联系人的内容 URI
                    null,                                    // 返回所有列
                    null,                                    // 不设置筛选条件
                    null,                                    // 无条件参数
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " asc" // 按姓名升序
            );
            if(cursor != null ){
                while(cursor.moveToNext()){
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contacts.add(name + "\n" + number);
                }
                adapter.notifyDataSetChanged();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}





















