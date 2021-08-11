package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FileChooserView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //高版本的系统可能不能获取到权限而不能显示


        rv=findViewById(R.id.rv);
        TextView tv=findViewById(R.id.path);
        rv.setOnFileItemClickListener(new FileChooserView.OnFileItemClickListener() {
            @Override
            public boolean onClick(FileBinder.VH vh, File file) {
                tv.setText(file.getAbsolutePath());
                return false;
            }

            @Override
            public void onRootPathChanged(File file) {
                tv.setText(file.getAbsolutePath());
            }
        });

        if (!(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){

            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(MainActivity.this,permissions, 1);
        }else {
            rv.setRootDir(Environment.getExternalStorageDirectory());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            rv.setRootDir(Environment.getExternalStorageDirectory());
        }else {
            Toast.makeText(this, "权限申请失败", Toast.LENGTH_SHORT).show();
        }
    }
}