package com.chenjunwei.library

import android.app.Application
import com.android.library.FileDownloader
import com.android.library.GreenDaoHelper
import com.android.library.connection.FileDownloadUrlConnection

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        GreenDaoHelper.initDB(this, "", "123456", true)

        initDownloader()
    }

    private fun initDownloader() {
        /**
         * just for cache Application's Context, and ':filedownloader' progress will NOT be launched
         * by below code, so please do not worry about performance.
         * @see FileDownloader.init
         */
        FileDownloader.setupOnApplicationOnCreate(this)
            .maxNetworkThreadCount(12)
            .connectionCreator(
                FileDownloadUrlConnection.Creator(
                    FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15000) // set connection timeout.
                        .readTimeout(15000) // set read timeout.
                )
            )
            .commit()
    }
}