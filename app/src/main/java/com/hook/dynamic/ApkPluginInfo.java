package com.hook.dynamic;

import dalvik.system.DexClassLoader;

public class ApkPluginInfo {
    private String dexPath;
    private DexClassLoader classLoader;

    public ApkPluginInfo(String dexPath, DexClassLoader classLoader) {
        this.dexPath = dexPath;
        this.classLoader = classLoader;
    }

    public String getDexPath() {
        return dexPath;
    }

    public DexClassLoader getClassLoader() {
        return classLoader;
    }
}


