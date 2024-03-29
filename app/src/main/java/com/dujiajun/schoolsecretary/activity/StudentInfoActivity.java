package com.dujiajun.schoolsecretary.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.dujiajun.schoolsecretary.MyDatabaseHelper;
import com.dujiajun.schoolsecretary.R;

import java.io.FileNotFoundException;

public class StudentInfoActivity extends AppCompatActivity {
    private static final int CROP_PHOTO = 2;
    private Toolbar toolbar;
    private EditText edit_name, edit_phone, edit_remark;
    private ImageButton img_btn1;
    private ImageView imgview1;
    private Uri imguri;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Boolean isEdit;
    private String originname;
    private String classname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        UIInit();
        dbHelper = new MyDatabaseHelper(this, "student.db", null, 2);
        db = dbHelper.getWritableDatabase();
        Intent i = getIntent();
        isEdit = i.getBooleanExtra("isEdit", false);
        classname = i.getStringExtra("classname");
        if (isEdit) {
            String name = i.getStringExtra("name");
            originname = name;
            String phone = i.getStringExtra("phone");
            String remark = i.getStringExtra("remark");
            if (!name.equals("")) {
                edit_name.setText(name);
            }
            if (!phone.equals("")) {
                edit_phone.setText(phone);
            }
            if (!remark.equals("")) {
                edit_remark.setText(remark);
            }
        }
        else{
            toolbar.setTitle("添加学生");
        }

    }

    protected void UIInit() {
        toolbar = (Toolbar) findViewById(R.id.std_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(StudentInfoActivity.this)
                        .setTitle("确定离开")
                        .setMessage("所做的修改将不会被保存，确定离开？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        edit_name = (EditText) findViewById(R.id.std_name_edit);
        edit_phone = (EditText) findViewById(R.id.std_phone_edit);
        edit_remark = (EditText) findViewById(R.id.std_remark_edit);
        img_btn1 = (ImageButton) findViewById(R.id.std_choose_pic_btn);
        img_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StudentInfoActivity.this, "该功能尚在开发中，敬请期待", Toast.LENGTH_SHORT).show();
                /*File file1 = new File(Environment.getExternalStorageDirectory(), "img.jpg");
                try {
                    if (file1.exists()) {
                        file1.delete();
                    }
                    file1.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imguri = Uri.fromFile(file1);
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                intent.putExtra("crop", true);
                intent.putExtra("scale", true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imguri);
                startActivityForResult(intent, CROP_PHOTO);*/
            }
        });
        imgview1 = (ImageView) findViewById(R.id.std_pic);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imguri));
                        imgview1.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.std_del: {
                if(isEdit) {
                    new AlertDialog.Builder(StudentInfoActivity.this)
                            .setTitle("确定删除")
                            .setMessage("删除后将不能恢复，确定删除？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.delete("students", "name = ? and classname = ?", new String[]{originname, classname});
                                    finish();
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }else{
                    new AlertDialog.Builder(StudentInfoActivity.this)
                            .setTitle("确定离开")
                            .setMessage("所做的修改将不会被保存，确定离开？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
                break;
            }
            case R.id.std_save:
                String name = edit_name.getText().toString();
                String phone = edit_phone.getText().toString();
                String remark = edit_remark.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(StudentInfoActivity.this, "没有填写姓名", Toast.LENGTH_SHORT).show();
                    return false;
                } else {

                    if(isEdit){
                        if(!name.equals(originname)){
                            Cursor cursor = db.query("students", null,
                                    "name = ? and classname = ?", new String[]{name, classname}, null, null, null);
                            if(cursor.getCount()!=0){
                                Toast.makeText(StudentInfoActivity.this, "已经存在学生"+name, Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        }
                        ContentValues values = new ContentValues();
                        values.put("name", name);
                        values.put("phone", phone);
                        values.put("remark", remark);
                        db.update("students", values, "name = ? and classname = ?", new String[]{originname, classname});
                    }else{

                        Cursor cursor = db.query("students", null,
                                "name = ? and classname = ?", new String[]{name, classname}, null, null, null);
                        if (cursor.getCount() != 0) {
                            Toast.makeText(StudentInfoActivity.this, "已经存在学生" + name, Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        ContentValues values = new ContentValues();
                        values.put("name", name);
                        values.put("phone", phone);
                        values.put("remark", remark);
                        values.put("classname", classname);
                        db.insert("students", null, values);
                    }

                    Toast.makeText(StudentInfoActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
