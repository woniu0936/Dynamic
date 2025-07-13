package com.hook.plugin01.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;

import com.hook.plugin01.R;

public class UIUtils {

    public static String getTextString(Context context) {
        return context.getResources().getString(R.string.plugin_hello_world);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public static Drawable getImageDrawable(Context context) {
        return context.getResources().getDrawable(R.mipmap.ic_launcher);
    }

    public static View getLayout(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.activity_resource, null);
    }

}
