package com.android.library.util;

import android.util.Log;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/09/21
 *     desc  : Log相关工具类
 * </pre>
 */
public final class LogUtil {

    private static final String TAG = "appplant";

    public static void log(String tag, String msg, int level) {
        if (AppUtils.isAppDebug()) {
            String msgs = msg;
            String tags = tag;
            Exception e = new Exception();
            StackTraceElement[] els = e.getStackTrace();
            String logDetails;
            for (int i = 0, l = els.length; i < l; i++) {
                if (!els[i].getClassName().equals(LogUtil.class.getName())) {
                    logDetails = els[i].getFileName() + "->"
                            + els[i].getMethodName() + ":"
                            + els[i].getLineNumber() + " ";
                    msgs = logDetails + msgs;
                    if ("".equals(tags)) {
                        tags = els[i].getFileName().substring(0,
                                els[i].getFileName().indexOf("."));
                    }
                    break;
                }
            }
            switch (level) {
                case Log.DEBUG:
                    Log.d(tags, msgs + "");
                    break;
                case Log.INFO:
                    Log.i(tags, msgs + "");
                    break;
                case Log.WARN:
                    Log.w(tags, msgs + "");
                    break;
                case Log.ERROR:
                    Log.e(tags, msgs + "");
                    break;
                default:
                    Log.d(tag, msgs + "");
                    break;
            }
        }
    }

    /**
     * Simple log
     *
     * @param tag
     * @param msg
     */
    public static void log(String tag, String msg) {
        log(tag, msg + "", Log.DEBUG);
    }

    public static void log(String msg) {
        log(TAG, msg + "");
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void i(String tag, String msg) {
        log(tag, msg + "", Log.INFO);
    }

    public static void e(String msg) {
        e(TAG, msg + "");
    }

    public static void e(String tag, String msg) {
        log(tag, msg + "", Log.ERROR);
    }

    public static void d(String msg) {
        d(TAG, msg + "");
    }

    public static void d(String tag, String msg) {
        log(tag, msg + "", Log.DEBUG);
    }
}
