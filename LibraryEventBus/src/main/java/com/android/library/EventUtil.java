package com.android.library;

import android.text.TextUtils;

import com.android.library.bean.EventBean;

import org.greenrobot.eventbus.EventBus;

/**
 * @Descripttion: 消息发送
 * @Author: 陈俊伟
 * @Date: 2020/11/17
 */
public class EventUtil {


    private static EventUtil mInstance;

    private EventUtil() {
        super();
    }

    public static EventUtil getInstance() {
        synchronized (EventUtil.class) {
            if (mInstance == null) {
                mInstance = new EventUtil();
            }
        }
        return mInstance;
    }

    /**
     * 事件过滤
     *
     * @param event
     * @param sign
     * @param action
     * @return
     */
    public static boolean isEvent(EventBean event, String sign, String action) {
        if (event != null && TextUtils.equals(sign, event.sign)
                && TextUtils.equals(action, event.action)) {
            return true;
        }
        return false;
    }

    /**
     * 注册消息事件
     *
     * @param subscriber
     */
    public void register(Object subscriber) {
        if (!EventBus.getDefault().isRegistered(subscriber))
            EventBus.getDefault().register(subscriber);
    }


    /**
     * 注销消息事件
     *
     * @param subscriber
     */
    public void unregister(Object subscriber) {
        if (EventBus.getDefault().isRegistered(subscriber))
            EventBus.getDefault().unregister(subscriber);
    }

    /**
     * 手动删除粘性广播
     *
     * @param object
     */
    public void removeSticky(Class<EventBean> object) {
        if (object != null) {
            EventBean mClass = EventBus.getDefault().getStickyEvent(object);
            if (mClass != null) {
                EventBus.getDefault().removeStickyEvent(object);
            }
        }
    }

    /**
     * 删除所有粘贴性事件
     */
    public void removeAllSticky() {
        EventBus.getDefault().removeAllStickyEvents();
    }

    /**
     * 发送事件（必需传入sign和action，强制使用），搭配isEvent()使用
     *
     * @param sign
     * @param action
     * @param object
     */
    public void post(String sign, String action, Object object) {
        EventBus.getDefault().post(new EventBean(sign, action, object));
    }
    /**
     * 发送事件，类内用onEventMainThread（权限是public）方法接收（这里是模块事件，不建议使用）
     *
     * @param object
     */
    public void post(Object object) {
        EventBus.getDefault().post(object);
    }
    /**
     * 当直接反射frgament的时候在fargment中有跳转了新的activity时候，
     * -->>由于在从activity再返回该frgament会造成当改frgament没有注册eventbus时就将消息传递过来
     * 这个时候就需要使用-->粘性发送事件(必须要ondesory的时候将调用removeSticky(...)方法或者removeAllSticky() )
     *
     * @param object
     */
    public void postSticky(String sign, String action, Object object) {
        EventBus.getDefault().postSticky(new EventBean(sign, action, object));
    }
}
