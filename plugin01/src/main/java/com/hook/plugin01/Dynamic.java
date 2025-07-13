package com.hook.plugin01;

import android.content.Context;

import com.hook.pluginlib.IDynamic;

public class Dynamic implements IDynamic {

    @Override
    public String getStringByResId(Context context) {
        return context.getString(R.string.plugin_hello_world);
    }
}
