package com.hook.dynamic;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hook.dynamic.utils.RefInvoke;

public class ResourceActivity extends BaseActivity {

    private TextView tvText;
    private ImageView ivIcon;
    private LinearLayout llLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);
        tvText = findViewById(R.id.tv_text);
        ivIcon = findViewById(R.id.iv_icon);
        llLayout = findViewById(R.id.ll_layout);
        findViewById(R.id.btn_01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 执行换肤功能
                loadResources();
                doStart();
            }
        });
    }

    private void doStart() {
        try {
            Class clazz = dexClassLoader.loadClass("com.hook.plugin01.utils.UIUtils");
            String str = RefInvoke.invokeStaticMethod(clazz, "getTextString", Context.class, this).toString();
            tvText.setText(str);
            Drawable img = (Drawable) RefInvoke.invokeStaticMethod(clazz, "getImageDrawable", Context.class, this);
            ivIcon.setImageDrawable(img);
            View layout = (View) RefInvoke.invokeStaticMethod(clazz, "getLayout", Context.class, this);
            llLayout.removeAllViews();
            llLayout.addView(layout);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
