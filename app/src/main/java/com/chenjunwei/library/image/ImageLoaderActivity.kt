package com.chenjunwei.library.image

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.library.CoreImageLoaderUtil
import com.chenjunwei.library.R
import kotlinx.android.synthetic.main.activity_image_loader.*

/**
 *@author chenjunwei
 *@desc 图片加载demo
 *@date 2019-09-26
 */
class ImageLoaderActivity : AppCompatActivity() {
    private val normalImageUrl = "https://cdn.pixabay.com/photo/2019/10/03/11/14/camp-4522970__340.jpg"
    private val gifImageUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1566554890720&" +
            "di=c64d60b20463ce7e3832c3879d1cf997&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20170805%2F19ad9705aec44c81af1760d40622c9fb.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_loader)
        initView()
    }

    private fun initView() {
        CoreImageLoaderUtil.loadingImg(this@ImageLoaderActivity, normalImageUrl, image1)
        CoreImageLoaderUtil.loadingGifImg(this@ImageLoaderActivity, gifImageUrl, image2)
        CoreImageLoaderUtil.loadingCircleImage(this@ImageLoaderActivity, normalImageUrl, image3, 20)
        CoreImageLoaderUtil.loadingImgWithAnim(this@ImageLoaderActivity, normalImageUrl, image4, R.anim.apha_anim)
    }

}
