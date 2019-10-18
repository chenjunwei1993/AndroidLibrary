package com.android.library.util;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/**
 * @author chenjunwei
 * @desc 圆角背景生产类
 * @date 2019-09-25
 */
public class ShapeUtil {
    /**
     * @param corner 圆角弧度
     * @param back   背景填充色
     * @return
     */
    public static Drawable getDrawable(int corner, int back) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(back);
        //左上，右上，右下，左下
        drawable.setCornerRadii(new float[]{corner, corner, corner, corner, corner, corner, corner, corner});
        //drawable.setStroke(Util.toDip(1), navbarBackground);
        return drawable;
    }

    /**
     * @param corner      圆角弧度
     * @param bgColor     背景填充色
     * @param strokeWidth 边框宽度
     * @param strokebg    边框颜色
     * @return
     */
    public static Drawable getDrawable(int corner, int bgColor, int strokeWidth, int strokebg) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(bgColor);
        //左上，右上，右下，左下
        drawable.setCornerRadii(new float[]{corner, corner, corner, corner, corner, corner, corner, corner});
        drawable.setStroke(strokeWidth, strokebg);
        return drawable;
    }


    /**
     * @param navbarBackground
     * @param navbarTitleColor
     * @param topLeft
     * @param bottomLeft
     * @param topRight
     * @param bottomRight
     * @return
     */
    public static Drawable getDrawable(int navbarBackground, int navbarTitleColor, int topLeft, int bottomLeft, int topRight, int bottomRight) {
        StateListDrawable bg = new StateListDrawable();
        GradientDrawable normal_drawable = new GradientDrawable();
        normal_drawable.setColor(0x00000000);
        normal_drawable.setCornerRadii(new float[]{topLeft, topLeft, topRight, topRight, bottomRight, bottomRight, bottomLeft, bottomLeft});
        normal_drawable.setStroke(SizeUtils.px2dp(1), navbarTitleColor);

        GradientDrawable checked_drawable = new GradientDrawable();
        checked_drawable.setColor(navbarTitleColor);
        //1、2两个参数表示左上角，3、4表示右上角，5、6表示右下角，7、8表示左下角
        checked_drawable.setCornerRadii(new float[]{topLeft, topLeft, topRight, topRight, bottomRight, bottomRight, bottomLeft, bottomLeft});
        //	checked_drawable.setStroke(Util.toDip(1), navbarBackground);

        bg.addState(new int[]{android.R.attr.state_checked}, checked_drawable);
        bg.addState(new int[]{}, normal_drawable);
        return bg;
    }
}
