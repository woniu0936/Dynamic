package com.hook.plugin01;

import com.hook.pluginlib.IBean;
import com.hook.pluginlib.ICallback;

import java.nio.channels.Pipe;

public class Bean implements IBean {

    private ICallback iCallback;

    @Override
    public void register(ICallback iCallback) {
        this.iCallback = iCallback;
        clickBtn();
    }

    public void clickBtn() {
        if (iCallback != null) {
            iCallback.sendResult("hello: " + name);
        }
    }

    private String name = "plugin01";

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
