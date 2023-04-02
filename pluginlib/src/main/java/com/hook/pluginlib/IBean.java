package com.hook.pluginlib;

public interface IBean {

    void setName(String name);

    String getName();

    void register(ICallback iCallback);

}
