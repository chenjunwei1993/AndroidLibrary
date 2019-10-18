package com.android.library.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author chenjunwei
 * @desc 资源相关的，View 的显示隐藏。 取String 或者Color
 * 设置TextVie，或EditText 的drawableLeft Top等的大小
 * @date 2019-09-25
 */
public class ResourceUtils {

    /**
     * 设置 View 显示状态
     *
     * @param view
     * @param flag
     */
    public static void setVisibility(View view, int flag) {
        if (view != null && view.getVisibility() != flag) {
            view.setVisibility(flag);
        }
    }

    /**
     * 获取颜色
     *
     * @param rid
     * @return
     */
    public static int getColor(int rid) {
        return getColor(CoreUtils.getContext(), rid);
    }

    /**
     * 获取颜色
     *
     * @param mContext
     * @param rid
     * @return
     */
    public static int getColor(Context mContext, int rid) {
        if (null == mContext) {
            return 0;
        }
        return mContext.getResources().getColor(rid);
    }

    /**
     * 获取指定 id 的String资源
     *
     * @param resourceId
     * @return
     */
    public static String getString(int resourceId) {
        return getString(CoreUtils.getContext(), resourceId);
    }

    /**
     * 获取指定 id 的String资源
     *
     * @param resourceId
     * @return
     */
    public static String getString(Context mContext, int resourceId) {
        if (null == mContext || mContext.getResources() == null) {
            LogUtil.e("资源获取失败");
            return "";
        }
        return mContext.getResources().getString(resourceId);
    }

    /**
     * 获取指定 id 的String[]资源
     *
     * @param resourceId
     * @return
     */
    public static String[] getStringArray(int resourceId) {
        return getStringArray(CoreUtils.getContext(), resourceId);
    }

    /**
     * 获取指定 id 的String[]资源
     *
     * @param mContext
     * @param resourceId
     * @return
     */
    public static String[] getStringArray(Context mContext, int resourceId) {
        if (null == mContext || mContext.getResources() == null) {
            LogUtil.e("资源获取失败");
            return null;
        }
        return mContext.getResources().getStringArray(resourceId);
    }


    /**
     * @param metaDataName meta_data 节点的key
     * @return 获取在application应用<meta-data>元素。
     */
    public static String getMetaData(String metaDataName) {
        return getMetaData(CoreUtils.getContext(), metaDataName);
    }

    /**
     * @return 获取在application应用<meta-data>元素。
     */
    public static String getMetaData(Context context, String metaDataName) {
        try {
            ApplicationInfo appInfo = context.getApplicationContext().getPackageManager()
                    .getApplicationInfo(context.getApplicationContext().getPackageName(),
                            PackageManager.GET_META_DATA);
            return String.valueOf(appInfo.metaData.getSerializable(metaDataName));
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.e(e.getMessage());
            return null;
        }
    }

    /**
     * 设置 TextView的左右图片大小
     *
     * @param view
     * @param width
     * @param height
     * @param attr   0 1 2 3 左上右下
     */
    public static void setCompoundDrawables(TextView view, int width,
                                            int height, int attr) {
        Drawable[] drawables = view.getCompoundDrawables();
        Drawable myImage = drawables[attr];
        if (myImage == null) {
            return;
        }
        myImage.setBounds(0, 0, width, height);
        switch (attr) {
            case 0:
                view.setCompoundDrawables(myImage, drawables[1], drawables[2],
                        drawables[3]);
                break;
            case 1:
                view.setCompoundDrawables(drawables[0], myImage, drawables[2],
                        drawables[3]);
                break;
            case 2:
                view.setCompoundDrawables(drawables[0], drawables[1], myImage,
                        drawables[3]);
                break;
            case 3:
                view.setCompoundDrawables(drawables[0], drawables[1], drawables[2],
                        myImage);
                break;
        }
    }

    /**
     * 设置EditText左右图片大小
     *
     * @param view
     * @param width
     * @param height
     * @param attr   0 1 2 3 左上右下
     */
    public static void setCompoundDrawables(EditText view, int width,
                                            int height, int attr) {
        Drawable[] drawables = view.getCompoundDrawables();
        Drawable myImage = drawables[attr];
        if (myImage == null) {
            return;
        }
        myImage.setBounds(0, 0, width, height);
        switch (attr) {
            case 0:
                view.setCompoundDrawables(myImage, drawables[1], drawables[2],
                        drawables[3]);
                break;
            case 1:
                view.setCompoundDrawables(drawables[0], myImage, drawables[2],
                        drawables[3]);
                break;
            case 2:
                view.setCompoundDrawables(drawables[0], drawables[1], myImage,
                        drawables[3]);
                break;
            case 3:
                view.setCompoundDrawables(drawables[0], drawables[1], drawables[2],
                        myImage);
                break;
        }
    }


}
