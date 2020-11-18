package com.android.library;

import android.content.Context;

import com.android.library.imageloader.R;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.target.ViewTarget;

/**
 * @Descripttion: 全局配置
 * @Author: 陈俊伟
 * @Date: 2020/11/17
 */
@GlideModule
public class GlideConfiguration extends AppGlideModule {
    public static String CACHEDIR = "glideCache";
    private final int ExternalCacheSize = 1024 * 1024 * 1000;

    @Override
    public void applyOptions(final Context context, GlideBuilder builder) {
        // Apply options to the builder here.
        ViewTarget.setTagId(R.id.glide_tag_id);
        // 设置Glide内存缓存的20%
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context).build();
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
        builder.setMemoryCache(new LruResourceCache(defaultMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(defaultBitmapPoolSize));
        // 设置磁盘缓存
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context,
                CACHEDIR, ExternalCacheSize));
    }

    /**
     * 返回false,关闭解析AndroidManifest
     * @return
     */
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
