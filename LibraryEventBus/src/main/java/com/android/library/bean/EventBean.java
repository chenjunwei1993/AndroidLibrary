package com.android.library.bean;

/**
 * @Descripttion: eventbus消息传递Bean
 * @Author: 陈俊伟
 * @Date: 2020/11/17
 */
public class EventBean {

    public String sign;
    public String action;
    public Object object;

    public EventBean(String sign, String action, Object object) {
        this.sign = sign;
        this.action = action;
        this.object = object;
    }

}
