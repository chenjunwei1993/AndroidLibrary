package com.chenjunwei.library

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chenjunwei.library.downloader.DownloaderActivity
import com.chenjunwei.library.greendao.GreenDaoActivity
import com.chenjunwei.library.image.ImageLoaderActivity
import com.chenjunwei.library.network.NetworkActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setListener()
    }

    private fun setListener() {
        btn_network.setOnClickListener { startActivity(Intent(this@MainActivity, NetworkActivity::class.java)) }
        btn_image_loader.setOnClickListener { startActivity(Intent(this@MainActivity, ImageLoaderActivity::class.java)) }
        btn_green_dao.setOnClickListener { startActivity(Intent(this@MainActivity, GreenDaoActivity::class.java)) }
        btn_downloader.setOnClickListener { startActivity(Intent(this@MainActivity, DownloaderActivity::class.java)) }
    }

}
