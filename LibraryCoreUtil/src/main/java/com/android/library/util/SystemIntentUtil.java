package com.android.library.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @author chenjunwei
 * @desc 系统意图工具类
 * @date 2019-09-25
 */
public class SystemIntentUtil {
    /**
     * 去评分
     */
    public static void goScoreAction(Context mContext) {
        try {
            Uri uri = Uri.parse("market://details?id="
                    + mContext.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开手机浏览器
     *
     * @param mContext
     * @param url
     */
    public static void openBrowser(Context mContext, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        mContext.startActivity(intent);
    }

    public static void Call(Context mContext, String phone) {
        Uri uri = Uri.parse("tel:" + phone);
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        mContext.startActivity(intent);
    }
}
