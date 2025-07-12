package com.hook.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hook.pluginlib.IBean;
import com.hook.pluginlib.ICallback;
import com.hook.pluginlib.IDynamic;

public class MainActivity extends BaseActivity {

    private TextView tvContent;
    private ApkPluginInfo mApkPluginInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvContent = findViewById(R.id.tv_content);
        mApkPluginInfo = getApkPluginInfo(pluginApkName);
        loadPluginObj();
        setOnBtn02Click();
        findViewById(R.id.btn_03).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResourceActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setOnBtn02Click() {
        findViewById(R.id.btn_02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadResources(mApkPluginInfo.getDexPath());
                Class mLoadClassDynamic = null;
                try {
                    mLoadClassDynamic = mApkPluginInfo.getClassLoader().loadClass("com.hook.plugin01.Dynamic");
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
                    Class<?> aClass = mApkPluginInfo.getClassLoader().loadClass("com.hook.plugin01.Bean");
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

}