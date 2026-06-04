package com.example.contentdemo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.contentdemo.utils.DBHelper;

public class MyContentProvider extends ContentProvider {
    private SQLiteDatabase db;

    public static final int STUDENT_DIR = 0;
    public static final int STUDENT_ITEM = 1;
    public static final String AUTHORITY = "com.example.contentdemo";

    private static UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "student", STUDENT_DIR);
        uriMatcher.addURI(AUTHORITY, "student/#", STUDENT_ITEM);
    }

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch(uriMatcher.match(uri)){
            case STUDENT_DIR:
                count=db.delete("t_student",selection,selectionArgs);
                break;
            case STUDENT_ITEM:
                String id = uri.getPathSegments().get(1);
                count = db.delete("t_student", "_id = ?", new String[]{id});
                break;

        }
        return count;
    }

    @Override
    public String getType(Uri uri) {
        // MIME type of the data at the given URI
        // MIME 类型说明 字符串取值 让Android系统能够知道处理的是整体数据 还是 单条记录
        // MIME字符串特点：以vnd开头
        // 如果uri以路径结尾 后面就接android.cursor.dir/
        // 如果uri以id结尾 后面就接android.cursor.item/
        // 最后接的内容为 vnd.<authority>.<path>
        switch (uriMatcher.match(uri)) {
            case STUDENT_DIR:
                return "vnd.android.cursor.dir/vnd." + AUTHORITY + ".student";
            case STUDENT_ITEM:
                return "vnd.android.cursor.item/vnd." + AUTHORITY + ".student";
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri newUri = null;
        long newId = 0;
        switch (uriMatcher.match(uri)){
            case STUDENT_DIR:
            case STUDENT_ITEM:
                newId = db.insert("t_student", null, values);
                newUri = Uri.parse("content://" + AUTHORITY + "/student/" + newId);
                break;
        }
        return newUri;
    }

    @Override
    public boolean onCreate() {
        DBHelper dbHelper = new DBHelper(getContext());
        db = dbHelper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case STUDENT_DIR:
                cursor = db.query("t_student", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case STUDENT_ITEM:
                String id = uri.getPathSegments().get(1);
                cursor = db.query("t_student", projection, "_id = ?", new String[]{id}, null, null, sortOrder);
                break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case STUDENT_DIR:
                count = db.update("t_student", values, selection, selectionArgs);
                break;
            case STUDENT_ITEM:
                String id = uri.getPathSegments().get(1);
                count = db.update("t_student", values, "_id = ?", new String[]{id});
        }
        return count;
    }
}