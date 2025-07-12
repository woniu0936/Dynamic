package com.hook.dynamic;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hook.dynamic.utils.Utils;
import com.hook.pluginlib.IBean;
import com.hook.pluginlib.ICallback;
import com.hook.pluginlib.IDynamic;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Dynamic";

    private String apkName = "plugin01-debug.apk";
    private String dexPath;
    private File fileRelease;
    private DexClassLoader dexClassLoader;
    private AssetManager mAssetManager;
    private Resources mResources;
    private Resources.Theme mTheme;
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

        fileRelease = getDir("dex", 0); // 0表示Context.MODE_PRIVATE
        Log.d(TAG, "dexPath: " + dexPath);
        Log.d(TAG, "fileRelease.getAbsolutePath(): " + fileRelease.getAbsolutePath());

        dexClassLoader = new DexClassLoader(dexPath, fileRelease.getAbsolutePath(), null, getClassLoader());

        tvContent = findViewById(R.id.tv_content);
        loadPluginObj();
        setOnBtn02Click();
    }

    private void setOnBtn02Click() {
        findViewById(R.id.btn_02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadResource();
                Class mLoadClassDynamic = null;
                try {
                    mLoadClassDynamic = dexClassLoader.loadClass("com.hook.plugin01.Dynamic");
                    Object dynamicObj = mLoadClassDynamic.newInstance();
                    IDynamic dynamic = (IDynamic) dynamicObj;
                    String content = dynamic.getStringByResId(MainActivity.this);
                    tvContent.setText(content);
                    Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    private void loadPluginObj() {
        findViewById(R.id.btn_01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Class<?> aClass = dexClassLoader.loadClass("com.hook.plugin01.Bean");
                    Object beanObj = aClass.newInstance();
                    IBean bean = (IBean) beanObj;
                    bean.register(new ICallback() {
                        @Override
                        public void sendResult(String result) {
                            tvContent.setText(result);
                            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    private void loadResource() {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexPath);
            mAssetManager = assetManager;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        mResources = new Resources(mAssetManager, super.getResources().getDisplayMetrics(), super.getResources().getConfiguration());
        mTheme = mResources.newTheme();
        mTheme.setTo(super.getTheme());
    }

    @Override
    public AssetManager getAssets() {
        if (mAssetManager == null) {
            return super.getAssets();
        }
        return mAssetManager;
    }

    @Override
    public Resources getResources() {
        if (mResources == null) {
            return super.getResources();
        }
        return mResources;
    }

    @Override
    public Resources.Theme getTheme() {
        if (mTheme == null) {
            return super.getTheme();
        }
        return mTheme;
    }
}