package com.hook.dynamic;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hook.dynamic.utils.Utils;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Dynamic";

    private String apkName = "plugin01-debug.apk";
    private String dexPath;
    private File fileRelease;
    private DexClassLoader dexClassLoader;
    private TextView tvContent;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        try {
            Utils.extractAssets(newBase, apkName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File fileStreamPath = getFileStreamPath(apkName);
        dexPath = fileStreamPath.getPath();

        fileRelease = getDir("dex", 0);
        Log.d(TAG, "dexPath: " + dexPath);
        Log.d(TAG, "fileRelease.getAbsolutePath(): " + fileRelease.getAbsolutePath());

        dexClassLoader = new DexClassLoader(dexPath, fileRelease.getAbsolutePath(), null, getClassLoader());

        tvContent = findViewById(R.id.tv_content);

        findViewById(R.id.btn_01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Class<?> aClass = dexClassLoader.loadClass("com.hook.plugin01.Bean");
                    Object beanObj = aClass.newInstance();
                    Method getNameMethod = aClass.getMethod("getName");
                    getNameMethod.setAccessible(true);
                    String name = (String) getNameMethod.invoke(beanObj);
                    tvContent.setText(name);
                    Toast.makeText(MainActivity.this, "name: " + name, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });

    }
}