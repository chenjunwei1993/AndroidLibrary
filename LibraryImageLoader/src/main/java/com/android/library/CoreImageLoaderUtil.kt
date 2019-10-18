package com.android.library

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.android.library.imageloader.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.android.library.GlideApp

/**
 *@author chenjunwei
 *@desc Glide工具类
 *@date 2019/8/23
 */
@SuppressLint("CheckResult")
object CoreImageLoaderUtil {
    /* --------------------------默认图片-------------------------- */
    var loading_50 = R.drawable.default_logo_50

    /**
     * 加载图片监听接口
     */
    interface LoadingImageListener {
        /**
         * 图片加载成功
         * @param t
         * @param <T>
         */
        fun <T> onResourceReady(t: T)

        /**
         * 图片加载失败
         */
        fun onLoadFailed()
    }

    /*----------------------------加载图片---------------------------*/

    /**
     * 加载本地图片到指定view
     * @param mContext  上下文
     * @param resource   本地图片地址/网络地址/bitmap等等;具体看Glide load方法
     * @param img   目标view
     */
    fun loadingImg(mContext: Context, resource: Any, img: ImageView) {
        loadingImg(mContext, resource, img, loading_50)
    }

    /**
     * 加载本地图片到指定view
     * @param mContext  上下文
     * @param resource   本地图片地址/网络地址/bitmap等等;具体看Glide load方法
     * @param img   目标view
     * @param placeholder   默认加载图
     */
    fun loadingImg(mContext: Context, resource: Any, img: ImageView, placeholder: Any?) {
        loadingImg(mContext, resource, img, placeholder, placeholder, null, 0, 0)
    }

    /**
     * 加载本地图片到指定view
     * @param mContext  上下文
     * @param resource   本地图片地址/网络地址/bitmap等等;具体看Glide load方法
     * @param img   目标view
     * @param placeholder   默认加载图
     * @param failResId   加载错误图
     * @param listener  加载监听
     * @param width     加载图片宽
     * @param height    加载图片高
     */
    fun loadingImg(
        mContext: Context, resource: Any, img: ImageView, placeholder: Any?,
        failResId: Any?, listener: LoadingImageListener?, width: Int, height: Int
    ) {
        loadingImg(mContext, resource, img, placeholder, failResId, listener, width, height, DiskCacheStrategy.ALL)
    }

    /**
     * 加载本地图片到指定view
     * @param mContext  上下文
     * @param resource   本地图片地址/网络地址/bitmap等等;具体看Glide load方法
     * @param img   目标view
     * @param placeholder   默认加载图
     * @param failResId   加载错误图
     * @param listener  加载监听
     * @param width     加载图片宽
     * @param height    加载图片高
     */
    fun loadingImg(
        mContext: Context, resource: Any, img: ImageView, placeholder: Any?, failResId: Any?,
        listener: LoadingImageListener?, width: Int, height: Int, diskCacheStrategy: DiskCacheStrategy
    ) {
        GlideApp.with(mContext).clear(img)
        val options = RequestOptions()
        if (width != 0 && height != 0) {
            options.override(width, height)
        }
        setPlaceHolder(placeholder, failResId, options)

        GlideApp.with(mContext)
            .asBitmap()
            .load(resource)
            .apply(options)
            .diskCacheStrategy(diskCacheStrategy)
            .dontAnimate()
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                    listener?.onLoadFailed()
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    listener?.onResourceReady(resource)
                    return false
                }

            })
            .into(img)
    }

    /**
     * 加载本地图片到指定view
     * @param mContext  上下文
     * @param resource   本地图片地址/网络地址/bitmap等等;具体看Glide load方法
     * @param placeholder   默认加载图
     * @param failResId   加载错误图
     * @param listener  加载监听
     * @param width     加载图片宽
     * @param height    加载图片高
     */
    fun loadingImg(
        mContext: Context, resource: Any, placeholder: Any?, failResId: Any?,
        listener: LoadingImageListener?, width: Int, height: Int
    ) {
        val options = RequestOptions()
        if (width != 0 && height != 0) {
            options.override(width, height)
        }
        setPlaceHolder(placeholder, failResId, options)
        GlideApp.with(mContext)
            .asBitmap()
            .load(resource)
            .apply(options)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                    listener?.onLoadFailed()
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    listener?.onResourceReady(resource)
                    return false
                }

            })
    }

    /**
     * 在非wifi环境下,不远程加载图片
     * 注：如果图片在内存或在磁盘缓存中，它会被展示出来。否则不展示；
     *
     * @param mContext  上下文
     * @param resource   本地图片地址/网络地址/bitmap等等;具体看Glide load方法
     * @param img   目标view
     */
    fun loadingImgOutOfWifi(mContext: Context, resource: Any, img: ImageView) {
        loadingImgOutOfWifi(mContext, resource, img, 0, 0)
    }

    /**
     * 在非wifi环境下,不远程加载图片
     * 注：如果图片在内存或在磁盘缓存中，它会被展示出来。否则不展示；
     *
     * @param mContext  上下文
     * @param resource   本地图片地址/网络地址/bitmap等等;具体看Glide load方法
     * @param img   目标view
     * @param placeholder   占位默认图片
     */
    fun loadingImgOutOfWifi(mContext: Context, resource: Any, img: ImageView, placeholder: Any?, failResId: Any?) {
        loadingImgOutOfWifi(mContext, resource, img, placeholder, failResId, 0, 0)
    }

    /**
     * 在非wifi环境下,不远程加载图片
     * 注：如果图片在内存或在磁盘缓存中，它会被展示出来。否则不展示；
     *
     * @param mContext  上下文
     * @param resource   本地图片地址/网络地址/bitmap等等;具体看Glide load方法
     * @param img   目标view
     * @param placeholder   占位默认图片
     * @param width     加载图片宽
     * @param height    加载图片高
     */
    fun loadingImgOutOfWifi(
        mContext: Context, resource: Any, img: ImageView, placeholder: Any?,
        failResId: Any?, width: Int, height: Int
    ) {
        loadingImgOutOfWifi(mContext, resource, img, placeholder, failResId, width, height, 0)
    }

    /**
     * 在非wifi环境下,不远程加载图片
     * 注：如果图片在内存或在磁盘缓存中，它会被展示出来。否则不展示；
     *
     * @param mContext  上下文
     * @param resource   本地图片地址/网络地址/bitmap等等;具体看Glide load方法
     * @param img   目标view
     * @param placeholder   占位默认图片
     * @param width     加载图片宽
     * @param height    加载图片高
     * @param imageLoaderAnim  是否需要动画
     */
    fun loadingImgOutOfWifi(
        mContext: Context, resource: Any, img: ImageView, placeholder: Any?,
        failResId: Any?, width: Int, height: Int, imageLoaderAnim: Int
    ) {
        GlideApp.with(mContext).clear(img)
        val options = RequestOptions()
        if (width != 0 && height != 0) {
            options.override(width, height)
        }
        options.diskCacheStrategy(DiskCacheStrategy.ALL)
            .onlyRetrieveFromCache(true)
        setPlaceHolder(placeholder, failResId, options)
        val mRequestBuilder = GlideApp.with(mContext).load(resource)
        if (imageLoaderAnim > 0) {
            mRequestBuilder.transition(DrawableTransitionOptions().transition(imageLoaderAnim))
        }
        mRequestBuilder.apply(options)
            .into(img)
    }

    /**
     * @param mContext  上下文
     * @param resource   本地图片地址/网络地址/bitmap等等;具体看Glide load方法
     * @param img   目标view
     * @param imageLoaderAnim  是否需要动画
     */
    fun loadingImgWithAnim(mContext: Context, resource: Any, img: ImageView, imageLoaderAnim: Int) {
        loadingImgWithAnim(mContext, resource, img, 0, 0, imageLoaderAnim)
    }

    /**
     * @param mContext  上下文
     * @param resource   本地图片地址/网络地址/bitmap等等;具体看Glide load方法
     * @param img   目标view
     * @param placeholder   占位默认图片
     * @param imageLoaderAnim  是否需要动画
     */
    fun loadingImgWithAnim(mContext: Context, resource: Any, img: ImageView, placeholder: Any?, failResId: Any?, imageLoaderAnim: Int) {
        loadingImgWithAnim(mContext, resource, img, placeholder, failResId, 0, 0, imageLoaderAnim)
    }

    /**
     * @param mContext  上下文
     * @param resource   本地图片地址/网络地址/bitmap等等;具体看Glide load方法
     * @param img   目标view
     * @param placeholder   占位默认图片
     * @param width     加载图片宽
     * @param height    加载图片高
     * @param imageLoaderAnim  是否需要动画
     */
    fun loadingImgWithAnim(
        mContext: Context, resource: Any, img: ImageView, placeholder: Any?,
        failResId: Any?, width: Int, height: Int, imageLoaderAnim: Int
    ) {
        GlideApp.with(mContext).clear(img)
        val options = RequestOptions()
        if (width != 0 && height != 0) {
            options.override(width, height)
        }
        setPlaceHolder(placeholder, failResId, options)
        val mRequestBuilder = GlideApp.with(mContext).load(resource)
        if (imageLoaderAnim > 0) {
            mRequestBuilder.transition(DrawableTransitionOptions().transition(imageLoaderAnim))
        }
        mRequestBuilder.apply(options)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(img)
    }

    /**
     * 加载圆形图片到指定view
     * @param mContext  上下文
     * @param resource   本地图片地址/网络地址/bitmap等等;具体看Glide load方法
     * @param imageView     目标view
     * @param radius    圆形图片半径
     */
    fun loadingCircleImage(mContext: Context, resource: Any, imageView: ImageView, radius: Int) {
        loadingCircleImage(mContext, resource, imageView, radius, loading_50)
    }

    /**
     * 加载圆形图片到指定view
     * @param mContext  上下文
     * @param resource   本地图片地址/网络地址/bitmap等等;具体看Glide load方法
     * @param imageView     目标view
     * @param radius    圆形图片半径
     */
    fun loadingCircleImage(mContext: Context, resource: Any, imageView: ImageView, radius: Int, placeholder: Any?) {
        loadingCircleImage(mContext, resource, imageView, radius, placeholder, 0, 0)
    }

    /**
     * 加载圆形图片到指定view
     * @param mContext  上下文
     * @param resource   本地图片地址/网络地址/bitmap等等;具体看Glide load方法
     * @param imageView     目标view
     * @param radius    圆形图片半径
     */
    fun loadingCircleImage(
        mContext: Context, resource: Any, imageView: ImageView, radius: Int,
        placeholder: Any?, width: Int, height: Int
    ) {
        loadingCircleImage(mContext, resource, imageView, radius, placeholder, placeholder, null, width, height, DiskCacheStrategy.ALL)
    }

    /**
     * 加载圆形图片到指定view
     * @param mContext  上下文
     * @param resource   本地图片地址/网络地址/bitmap等等;具体看Glide load方法
     * @param imageView     目标view
     * @param radius    圆形图片半径
     * @param placeholder   占位默认图片
     * @param listener  加载监听
     */
    fun loadingCircleImage(
        mContext: Context, resource: Any, imageView: ImageView, radius: Int, placeholder: Any?, failResId: Any?,
        listener: LoadingImageListener?, width: Int, height: Int, diskCacheStrategy: DiskCacheStrategy
    ) {
        GlideApp.with(mContext).clear(imageView)
        val options = RequestOptions()
        if (width != 0 && height != 0) {
            options.override(width, height)
        }
        setPlaceHolder(placeholder, failResId, options)
        GlideApp.with(mContext)
            .asBitmap()
            .load(resource)
            .apply(options)
            .diskCacheStrategy(diskCacheStrategy)
            .transforms(CenterCrop(), RoundedCorners(radius))
            .into(object : BitmapImageViewTarget(imageView) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    super.onResourceReady(resource, transition)
                    listener?.onResourceReady(resource)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    listener?.onLoadFailed()
                }
            })
    }

    /*----------------------------加载gif图片---------------------------*/

    /**
     * 加载本地图片到指定view
     * @param mContext  上下文
     * @param resource   本地图片地址/网络地址/bitmap等等;具体看Glide load方法
     * @param img   目标view
     */
    fun loadingGifImg(mContext: Context, resource: Any, img: ImageView) {
        loadingGifImg(mContext, resource, img, 0, 0)
    }

    /**
     * 加载本地图片到指定view
     * @param mContext  上下文
     * @param resource   gif路径
     * @param img   目标view
     * @param placeholder   默认加载图
     * @param failResId   加载错误图
     */
    fun loadingGifImg(mContext: Context, resource: Any, img: ImageView, placeholder: Any?, failResId: Any?) {
        loadingGifImg(mContext, resource, img, placeholder, failResId, null)
    }

    fun loadingGifImg(
        mContext: Context, resource: Any, img: ImageView, placeholder: Any?,
        failResId: Any?, listener: LoadingImageListener?
    ) {
        loadingGifImg(mContext, resource, img, 0, 0, placeholder, failResId, listener)
    }

    /**
     * 加载本地图片到指定view
     * @param mContext  上下文
     * @param resource   gif路径
     * @param img   目标view
     * @param placeholder   默认加载图
     * @param failResId   加载错误图
     * @param listener  加载监听
     * @param width     加载图片宽
     * @param height    加载图片高
     */
    fun loadingGifImg(
        mContext: Context, resource: Any, img: ImageView, width: Int,
        height: Int, placeholder: Any?, failResId: Any?, listener: LoadingImageListener?
    ) {
        GlideApp.with(mContext).clear(img)
        val options = RequestOptions()
        if (width != 0 && height != 0) {
            options.override(width, height)
        }
        setPlaceHolder(placeholder, failResId, options)
        GlideApp.with(mContext)
            .asGif()
            .load(resource)
            .apply(options)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .listener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {
                    listener?.onLoadFailed()
                    return false
                }

                override fun onResourceReady(resource: GifDrawable?, model: Any?, target: Target<GifDrawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    listener?.onResourceReady(resource)
                    return false
                }

            })
            .into(img)
    }


    /**
     * 设置默认、错误 加载图
     * @param placeholder   默认加载图
     * @param failResId     错误加载图
     * @param options
     */
    private fun setPlaceHolder(placeholder: Any?, failResId: Any?, options: RequestOptions) {
        if (null != placeholder) {
            if (placeholder is Drawable) {
                options.placeholder(placeholder)
            } else if (placeholder is Int) {
                if (placeholder > 0) {
                    options.placeholder(placeholder)
                }
            }
        }
        if (null != failResId) {
            if (failResId is Drawable) {
                options.placeholder(failResId)
            } else if (failResId is Int) {
                if (failResId > 0) {
                    options.placeholder(failResId)
                }
            }
        }
    }

    /**
     * 清除图片内存缓存
     * @param mContext
     */
    fun clearMemory(mContext: Context) {
        Glide.get(mContext).clearMemory()
    }

    /**
     * 清除图片磁盘缓存
     * @param mContext
     */
    fun clearDishCache(mContext: Context) {
        GlideApp.get(mContext).clearDiskCache()
    }

}