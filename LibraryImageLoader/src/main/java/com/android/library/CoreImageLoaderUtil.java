package com.android.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;


/**
 * 图片加载工具类
 *
 * @author lixurong
 * @date 2017/9/7
 */

public class CoreImageLoaderUtil {
    /* -------------------------接口---------------------------- */

    public interface LoadingImageListener {
        /**
         * 图片加载成功
         *
         * @param t
         * @param <T>
         */
        <T> void onResourceReady(T t);

        /**
         * 加载失败
         */
        void onLoadFailed();
    }

    /**
     * ----------------------------加载图片 int---------------------------
     */
    public static void loadingImg(Context mContext, int resouse, ImageView img,
                                  int placeholder) {
        RequestOptions options = new RequestOptions().placeholder(placeholder)
                .error(placeholder)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        GlideApp.with(mContext).load(resouse).apply(options).into(img);
    }

    /**
     * ----------------------------加载本地gif图片---------------------------
     */
    public static void loadingGifImg(Context mContext, int rid, ImageView img) {
        GlideApp.with(mContext).clear(img);
        RequestOptions options = new RequestOptions()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        GlideApp.with(mContext).asGif().load(rid).apply(options).into(img);
    }
    /**
     * ----------------------------加载本地sd卡图片---------------------------
     */
    public static void loadingLocalImg(Context mContext, String path, final ImageView img, final int placeholder, final LoadingImageListener listener) {
        RequestOptions options = new RequestOptions().placeholder(placeholder)
                .error(placeholder)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        GlideApp.with(mContext).asBitmap().load(path).apply(options).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                if(listener != null){
                    listener.onResourceReady(resource);
                }else{
                    img.setImageBitmap(resource);
                }
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                if(listener != null){
                    listener.onLoadFailed();
                }else{
                    img.setImageResource(placeholder);
                }
            }
        });
    }

    /**
     * ----------------------------加载图片 Bitmap---------------------------
     */
    public static void loadingImg(Context mContext, Bitmap bitmap,
                                  ImageView img, int placeholder) {
        GlideApp.with(mContext).clear(img);
        RequestOptions options = new RequestOptions().placeholder(placeholder)
                .error(placeholder)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        GlideApp.with(mContext).asBitmap().load(bitmap).apply(options).into(img);
    }

    /**
     * --------------------------加载图片File--------------------------------------
     */
    public static void loadingImg(Context mContext, File file, ImageView img,
                                  int placeholder) {
        GlideApp.with(mContext).clear(img);
        RequestOptions options = new RequestOptions().placeholder(placeholder)
                .error(placeholder)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        GlideApp.with(mContext).asFile().load(file).apply(options).into(img);
    }

    /**
     * --------------------------单独下载加载图片--------------------------------------
     */
    public static void loadingImg(final Context mContext, String url,
                                  final LoadingImageListener listener, int width, int height) {
        GlideApp.with(mContext)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        listener.onResourceReady(resource);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
//                        GlideApp.with(mContext).clear(this);
                        listener.onLoadFailed();
                    }
                });
    }

    /**
     * --------------------------加载网络普通图片-----------------------------------
     */
    public static void loadingImgSimple(Context mContext, String url, ImageView img) {
        GlideApp.with(mContext).load(url).into(img);
    }

    /**
     * @param mContext
     * @param url
     * @param img
     * @param placeholder
     * @param listener
     * @param withAnim
     * @param width
     * @param height
     */
    public static void loadingImgWithCommon(final Context mContext,
                                            String url, final ImageView img, final int placeholder,
                                            final LoadingImageListener listener, boolean withAnim, int width,
                                            int height, int imageloader_anim) {
        GlideApp.with(mContext).clear(img);
        RequestOptions options = new RequestOptions().placeholder(placeholder)
                .error(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        RequestBuilder<Drawable> mRequestBuilder = GlideApp.with(mContext)
                .load(url);
        //glide v4动画有问题，先注销
//        if (withAnim) {
//            mRequestBuilder.transition(withCrossFade(200));
//        } else {
            options.dontAnimate();
//        }
        if (width != 0 && height != 0) {
            options.override(width, height);
        }
        mRequestBuilder.apply(options);
        if (listener != null) {
            mRequestBuilder.listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    listener.onLoadFailed();
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    listener.onResourceReady(resource);
                    return false;
                }
            });
        }
        mRequestBuilder.into(img);
    }

    /**
     * 非wifi环境下 不显示图片
     *
     * @param mContext
     * @param url
     * @param img
     * @param placeholder
     */
    public static void loadingImgOutofWifi(Context mContext, String url,
                                           final ImageView img, int placeholder, int imageloader_anim) {
        GlideApp.with(mContext).clear(img);
        RequestOptions options = new RequestOptions().placeholder(placeholder)
                .error(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        GlideApp.with(mContext)
                .load(placeholder)
                .apply(options).into(img);
    }

    /**
     * listview 加载圆形图片
     *
     * @param mContext
     * @param url
     * @param imageView 直接ImageView
     * @param radius    半径，如果是圆形，半径即为宽高的一般，只需要圆角，自定义
     */
    public static void loadingCircleImage(final Context mContext, String url,
                                          final ImageView imageView, final int radius,
                                          final Drawable placeholder, final LoadingImageListener listener) {
        GlideApp.with(mContext).clear(imageView);
        imageView.setImageDrawable(placeholder);
        RequestOptions options = new RequestOptions().placeholder(placeholder)
                .error(placeholder).centerCrop();
        GlideApp.with(mContext).asBitmap().load(url).apply(options)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory
                                .create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCornerRadius(radius);
                        imageView.setImageDrawable(circularBitmapDrawable);
                        if (listener != null) {
                            listener.onResourceReady(circularBitmapDrawable);
                        }
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        imageView.setImageDrawable(placeholder);
                        if (listener != null) {
                            listener.onLoadFailed();
                        }
                    }
                });
    }

    /**
     * 清除图片缓存
     *
     * @param mContext
     */
    public static void clearMemory(final Context mContext) {
        GlideApp.get(mContext).clearMemory();
    }

    public static void clearDiskCache(final Context mContext) {
        GlideApp.get(mContext).clearDiskCache();
    }

}
