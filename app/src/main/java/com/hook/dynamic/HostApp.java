package com.hook.dynamic;

import android.app.Application;
import android.content.Context;

public class HostApp extends Application {

    private static HostApp mInstance;

    public static Context getContext() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance  = this;
    }
}
