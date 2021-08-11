package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //你需要手动授予app文件访问权限
        FileChooserView rv=findViewById(R.id.rv);
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
        rv.setRootDir(Environment.getExternalStorageDirectory());
    }


}