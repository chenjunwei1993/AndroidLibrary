package com.chenjunwei.library.downloader

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.library.BaseDownloadTask
import com.android.library.FileDownloadListener
import com.android.library.FileDownloadQueueSet
import com.android.library.FileDownloader
import com.android.library.util.FileDownloadUtils
import com.chenjunwei.library.R
import kotlinx.android.synthetic.main.activity_downloader.*
import java.io.File
import java.util.*

class DownloaderActivity : AppCompatActivity() {
    private val TAG = "DownloaderActivity"
    private var totalCounts = 0
    private var finalCounts = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_downloader)
        setListener()
    }

    private fun setListener() {
        btn_delete_downloader.setOnClickListener {
            deleteFile()
        }

        btn_single_downloader.setOnClickListener {
            onClickStartSingleDownload()
        }

        btn_multi_serial_downloader.setOnClickListener {
            onClickMultiSerial()
        }

        btn_multi_parallel_downloader.setOnClickListener {
            onClickMultiParallel()
        }
    }

    private fun deleteFile() {
        val file = File(FileDownloadUtils.getDefaultSaveRootPath())
        if (!file.exists()) {
            Log.w(TAG, String.format("check file files not exists %s", file.absolutePath))
            return
        }

        if (!file.isDirectory) {
            Log.w(TAG, String.format("check file files not directory %s", file.absolutePath))
            return
        }

        val files = file.listFiles()

        if (files == null) {
            updateDisplay(getString(R.string.del_file_error_empty))
            return
        }

        for (file1 in files!!) {
            file1.delete()
            updateDisplay(getString(R.string.deleted_file, file1.name))
        }
    }

    /**
     * Start single download task
     *
     *
     * 启动单任务下载
     *
     */
    private fun onClickStartSingleDownload() {
        updateDisplay(getString(R.string.start_single_task, Constant.BIG_FILE_URLS[2]))
        totalCounts++
        FileDownloader.getImpl().create(Constant.BIG_FILE_URLS[2])
            .setListener(createListener())
            .setTag(1)
            .start()
    }

    /**
     * Start multiple download tasks parallel
     *
     *
     * 启动并行多任务下载
     *
     */
    private fun onClickMultiParallel() {
        updateDisplay(getString(R.string.start_multiple_tasks_parallel, Constant.URLS.size))

        // 以相同的listener作为target，将不同的下载任务绑定起来
        val parallelTarget = createListener()
        val taskList = ArrayList<BaseDownloadTask>()
        var i = 0
        for (url in Constant.URLS) {
            taskList.add(
                FileDownloader.getImpl().create(url)
                    .setPath(FileDownloadUtils.getDefaultSaveRootPath() + "/file" + ++i + ".jpg")
                    .setTag(++i)
            )
        }
        totalCounts += taskList.size

        FileDownloadQueueSet(parallelTarget)
            .setForceReDownload(true)
            .setAutoRetryTimes(1)
            .setCallbackProgressTimes(1)
            .downloadTogether(taskList)
            .start()
    }

    /**
     * Start multiple download tasks serial
     *
     *
     * 启动串行多任务下载
     *
     */
    private fun onClickMultiSerial() {
        updateDisplay(getString(R.string.start_multiple_tasks_serial, Constant.URLS.size))

        // 以相同的listener作为target，将不同的下载任务绑定起来
        val taskList = ArrayList<BaseDownloadTask>()
        val serialTarget = createListener()
        var i = 0
        for (url in Constant.URLS) {
            taskList.add(
                FileDownloader.getImpl().create(url)
                    .setPath(FileDownloadUtils.getDefaultSaveRootPath() + "/file" + ++i + ".jpg")
                    .setTag(i)
            )
        }
        totalCounts += taskList.size

        FileDownloadQueueSet(serialTarget)
            .setCallbackProgressTimes(1)
            .setForceReDownload(true)
            .setAutoRetryTimes(1)
            .downloadSequentially(taskList)
            .start()
    }

    private fun createListener(): FileDownloadListener {
        return object : FileDownloadListener() {
            override fun pending(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                task?.run {
                    updateDisplay(String.format("[pending] id[%d] %d/%d", id, soFarBytes, totalBytes))
                }
            }

            override fun connected(task: BaseDownloadTask?, etag: String?, isContinue: Boolean, soFarBytes: Int, totalBytes: Int) {
                super.connected(task, etag, isContinue, soFarBytes, totalBytes)
                task?.run {
                    updateDisplay(String.format("[connected] id[%d] %s %B %d/%d", id, etag, isContinue, soFarBytes, totalBytes))
                }
            }

            override fun progress(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                task?.run {
                    updateDisplay(String.format("[progress] id[%d] %d/%d", id, soFarBytes, totalBytes))
                }
            }

            override fun blockComplete(task: BaseDownloadTask?) {
                task?.run {
                    download_msg_tv.post { updateDisplay(String.format("[blockComplete] id[%d]", id)) }
                }
            }

            override fun retry(task: BaseDownloadTask?, ex: Throwable?, retryingTimes: Int, soFarBytes: Int) {
                super.retry(task, ex, retryingTimes, soFarBytes)
                task?.run {
                    updateDisplay(String.format("[retry] id[%d] %s %d %d", id, ex, retryingTimes, soFarBytes))
                }

            }

            override fun completed(task: BaseDownloadTask?) {
                task?.run {
                    Log.e("filePath", filename)
                    finalCounts++
                    updateDisplay(String.format("[completed] id[%d] oldFile[%B]", id, isReusedOldFile))
                    updateDisplay(String.format("---------------------------------- %d", tag as Int))
                }

            }

            override fun paused(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                task?.run {
                    finalCounts++
                    updateDisplay(String.format("[paused] id[%d] %d/%d", id, soFarBytes, totalBytes))
                    updateDisplay(String.format("############################## %d", tag as Int))
                }
            }

            override fun error(task: BaseDownloadTask?, e: Throwable?) {
                task?.run {
                    finalCounts++
                    updateDisplay(Html.fromHtml(String.format("[error] id[%d] %s %s", id, e, FileDownloadUtils.getStack(e?.stackTrace, false))))
                    updateDisplay(String.format("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! %d", tag as Int))
                }

            }

            override fun warn(task: BaseDownloadTask?) {
                task?.run {
                    finalCounts++
                    updateDisplay(String.format("[warn] id[%d]", id))
                    updateDisplay(String.format("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ %d", tag as Int))
                }
            }
        }
    }

    private fun updateDisplay(msg: CharSequence) {
        if (download_msg_tv.lineCount > 2500) {
            download_msg_tv.text = ""
        }
        download_msg_tv.append(String.format("\n %s", msg))
        tip_msg_tv.text = String.format("%d/%d", finalCounts, totalCounts)
        scrollView.post(scroll2Bottom)
    }

    private val scroll2Bottom = Runnable {
        scrollView?.fullScroll(View.FOCUS_DOWN)
    }

    override fun onDestroy() {
        super.onDestroy()
        FileDownloader.getImpl().pauseAll()
    }
}