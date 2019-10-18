package com.android.library.util;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.library.coreutil.R;


/**
 * Toast弹窗 传0就不显示图片只显示文字
 *
 * @author WANGYA
 */
public class CustomToast {
    /**
     * 警告类型
     */
    public final static int WARM = 100;
    /**
     * 失败类型
     */
    public final static int FAILURE = 101;
    /**
     * 成功类型
     */
    public final static int SUCCESSS = 102;
    /**
     * 不显示
     */
    public final static int DEFAULT = 0;

    /**
     * 特殊类型：积分
     */
    public final static int SOCRE = 201;
    private static Toast toast;

    /**
     * toast消失回调监听
     */
    public interface ToastListener {
        /**
         * 消失
         */
        void onToastDismiss();
    }

    /**
     * 直接显示Toast
     */
    public static void showToast(Context mContext, String msg) {
        showToast(mContext, msg, DEFAULT);
    }

    public static void showToast(Context mContext, String msg, ToastListener listener) {
        showToast(mContext, msg, DEFAULT);
    }

    /**
     * 直接显示Toast
     *
     * @param context
     * @param resId
     * @param duration
     * @param type
     */
    public static void showToast(Context context, int resId, int duration,
                                 int type) {
        showToast(context, ResourceUtils.getString(resId), duration, type, null);
    }

    /**
     * 直接显示Toast
     *
     * @param context
     * @param strId
     * @param type
     */
    public static void showToast(Context context, int strId, int type) {
        showToast(context, ResourceUtils.getString(strId), type);
    }

    /**
     * 直接显示Toast
     *
     * @param context
     * @param text
     * @param type
     */
    public static void showToast(Context context, String text, int type) {
        showToast(context, text, 2500, type, null);
    }

    /**
     * 直接显示Toast
     *
     * @param context
     * @param text
     * @param type
     */
    public static void showToast(Context context, String text, int duration, int type) {
        showToast(context, text, duration, type, null);
    }

    /**
     * 直接显示Toast
     *
     * @param context
     * @param text
     * @param type
     */
    public static void showToast(Context context, String text, int type, ToastListener listener) {
        showToast(context, text, 2500, type, listener);
    }

    /**
     * 直接显示Toast
     *
     * @param context
     * @param text
     * @param duration
     * @param type
     */
    public static void showToast(Context context, String text, int duration,
                                 int type, ToastListener listener) {
        showToast(context, text, null, duration, type, listener);
    }

    /**
     * 直接显示Toast
     *
     * @param context
     * @param text
     * @param imgurl
     * @param duration
     * @param type
     */
    public static void showToast(Context context, String text, String imgurl,
                                 int duration, int type) {
        showToast(context, text, null, duration, type, null);
    }

    /**
     * 直接显示Toast
     *
     * @param context
     * @param text
     * @param imgurl
     * @param duration
     * @param type
     * @param listener 消失事件监听
     */
    public static void showToast(Context context, String text, String imgurl,
                                 int duration, int type, ToastListener listener) {
        if (context == null || TextUtils.isEmpty(text)) {
            return;
        }
        if ((!AppUtils.isAppDebug() && text.toLowerCase().contains("exception")
                || text.toLowerCase().contains("error"))) {
            return;
        }
        Toast toast = makeText(context, text, imgurl, duration, type, listener);
        if (toast != null) {
            toast.show();
        }
    }

    /**
     * 生成toast布局,无需监听消失事件
     *
     * @param context
     * @param text
     * @param imgUrl
     * @param duration
     * @param type
     * @return
     */
    private static Toast makeText(Context context, String text, String imgUrl,
                                  int duration, int type) {
        return makeText(context, text, imgUrl, duration, type, null);
    }

    /**
     * 生成toast布局
     *
     * @param context
     * @param text
     * @param imgUrl
     * @param duration
     * @param type
     * @param listener 消失事件监听
     * @return
     */
    private static Toast makeText(Context context, String text, String imgUrl,
                                  int duration, int type, final ToastListener listener) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return null;
        }
        // 禁止弹出类似403的错误 updata by wangya 2016-07-15 15:29:09
        if (text.contains("<html>") || text.contains("403")) {
            return null;
        }
        if (!TextUtils.equals("English", AppUtils.getAppLanguage()) && !CheckUtil.isContainsChinese(text)) {
            return null;
        }

        if (toast != null) {
            toast.cancel();
        }
        DisplayMetrics dm = context.getApplicationContext().getResources()
                .getDisplayMetrics();
        int height = dm.heightPixels;
        RelativeLayout tostview = (RelativeLayout) LinearLayout.inflate(
                context.getApplicationContext(), R.layout.custom_toast, null);
        TextView textView = (TextView) tostview.findViewById(R.id.message);
        ImageView image = (ImageView) tostview.findViewById(R.id.image);
        textView.setText(text);
        toast = new Toast(context.getApplicationContext());
        toast.setView(tostview);
        toast.setDuration(duration);
        toast.setGravity(Gravity.CENTER, 0, height / 5);
        if (!TextUtils.isEmpty(imgUrl)) {
//            ImageLoaderUtil.loadingImg(context, imgUrl, image);
        } else {
            setToastDrawable(context, type, image);
        }
        setToastAnim(toast);
        textView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                if (listener != null) {
                    listener.onToastDismiss();
                }
            }
        });
        return toast;
    }

    /**
     * 统一 Toast 动画，使 Toast 动画不再因不同系统而产生差异
     */
    public static void setToastAnim(Toast toast) {
        if (toast != null) {
            // Toast 动画
            try {
                Object mTN = ReflectUtil.getField(toast, "mTN");
                if (mTN != null) {
                    Object mParams = ReflectUtil.getField(mTN, "mParams");
                    if (mParams != null
                            && mParams instanceof WindowManager.LayoutParams) {
                        WindowManager.LayoutParams params = (WindowManager.LayoutParams) mParams;
                        params.windowAnimations = R.style.AnimScale;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void setToastDrawable(Context context, int type,
                                         ImageView image) {
        // 关闭所有表情 update by wangya 2016-07-15 15:29:28
        ResourceUtils.setVisibility(image, View.GONE);
        // int resouse = DEFAULT;
        // switch (type) {
        // case WARM:
        // resouse = R.drawable.toast_warm;
        // break;
        // case FAILURE:
        // resouse = R.drawable.toast_failure;
        // break;
        // case SUCCESSS:
        // resouse = R.drawable.toast_success;
        // break;
        // case SOCRE:
        // resouse = R.drawable.share_add_score_big;
        // break;
        // default:
        // break;
        // }
        // if (resouse != DEFAULT) {
        // image.setImageResource(resouse);
        // ThemeUtil.setImageResource(context, image, resouse);
        // Util.setVisibility(image, View.VISIBLE);
        // }
    }
}
