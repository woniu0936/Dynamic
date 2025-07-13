package com.hook.dynamic;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hook.dynamic.utils.Utils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;

public class BaseActivity extends AppCompatActivity {

    public static final String TAG = "Dynamic";

    protected AssetManager mAssetManager;
    protected Resources mResources;
    protected Resources.Theme mTheme;
    protected File fileRelease = null;  //释放目录

    protected Map<String, ApkPluginInfo> mApkPluginInfos = new HashMap<>();

    protected String pluginApkName = "plugin01-debug.apk";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadPluginApk(pluginApkName);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        try {
            Utils.extractAssets(newBase, pluginApkName);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void loadPluginApk(String pluginApkName) {
        File extractFile = this.getFileStreamPath(pluginApkName);
        String dexPath = extractFile.getPath();
        fileRelease = getDir("dex", 0);
        DexClassLoader dexClassLoader = new DexClassLoader(dexPath, fileRelease.getAbsolutePath(), null, getClassLoader());
        Log.d(TAG, "dexPath: " + dexPath);
        Log.d(TAG, "fileRelease.getAbsolutePath(): " + fileRelease.getAbsolutePath());
        ApkPluginInfo info = new ApkPluginInfo(dexPath, dexClassLoader);
        mApkPluginInfos.put(pluginApkName, info);
    }

    protected ApkPluginInfo getApkPluginInfo(String apkName) {
        return mApkPluginInfos.get(apkName);
    }

    protected void loadResources(String dexPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexPath);
            mAssetManager = assetManager;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        Resources superRes = super.getResources();
        superRes.getDisplayMetrics();
        superRes.getConfiguration();
        mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        mTheme = mResources.newTheme();
        mTheme.setTo(super.getTheme());
    }

    @Override
    public AssetManager getAssets() {
        return mAssetManager == null ? super.getAssets() : mAssetManager;
    }

    @Override
    public Resources getResources() {
        return mResources == null ? super.getResources() : mResources;
    }

    @Override
    public Resources.Theme getTheme() {
        return mTheme == null ? super.getTheme() : mTheme;
    }

}
