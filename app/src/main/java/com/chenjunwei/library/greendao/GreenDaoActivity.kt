package com.chenjunwei.library.greendao

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.library.GreenDaoHelper
import com.android.library.bean.DBDetailBean
import com.chenjunwei.library.R
import kotlinx.android.synthetic.main.activity_greendao.*

/**
 *@author chenjunwei
 *@desc greendao
 *@date 2019-09-26
 */
class GreenDaoActivity : AppCompatActivity() {
    private val stringBuilder = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greendao)
        setListener()
    }

    private fun setListener() {
        btn_insert.setOnClickListener {
            val dbDetailBean = DBDetailBean()
            dbDetailBean.url = "www.baidu.com"
            dbDetailBean.data = "测试数据"
//            dbDetailBean.test = "测试数据库变更"
            GreenDaoHelper.insert(dbDetailBean)
        }

        btn_query.setOnClickListener {
            stringBuilder.clear()
            val dbDetailBeanList = GreenDaoHelper.findAll<DBDetailBean>(DBDetailBean::class.java)
            dbDetailBeanList.forEach {
                stringBuilder.append("url=${it.url};data=${it.data}")
            }
            tv_query_list.text = stringBuilder.toString()
        }

    }

}
