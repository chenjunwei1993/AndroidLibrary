package com.android.library

import android.content.Context
import com.android.library.imageloader.R
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.target.ViewTarget

@GlideModule
class GlideConfiguration : AppGlideModule() {
    companion object{
        const val CACHE_DIR = "glideCache"
        const val EXTERNAL_CACHE_SIZE = 1024 * 1024 * 1000
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        ViewTarget.setTagId(R.id.glide_tag_id)
        // 设置Glide内存缓存的20%
        val calculator = MemorySizeCalculator.Builder(context).build()
        val defaultMemoryCacheSize = calculator.memoryCacheSize
        val defaultBitmapPoolSize = calculator.bitmapPoolSize

        //内存缓存
        builder.setMemoryCache(LruResourceCache(defaultMemoryCacheSize.toLong()))
        //Bitmap 池
        builder.setBitmapPool(LruBitmapPool(defaultBitmapPoolSize.toLong()))
        // 设置磁盘缓存
        builder.setDiskCache(
            ExternalCacheDiskCacheFactory(
                context,
                CACHE_DIR,
                EXTERNAL_CACHE_SIZE
            )
        )
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}