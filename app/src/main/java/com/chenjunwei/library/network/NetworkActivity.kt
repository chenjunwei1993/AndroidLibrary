package com.chenjunwei.library.network

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.library.Api
import com.android.library.BaseRspObserver
import com.chenjunwei.library.BuildConfig
import com.chenjunwei.library.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_network.*
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
*@author chenjunwei
*@desc 网络请求demo
*@date 2019-09-26
*/
class NetworkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)
        initView()
    }

    private fun initView() {
        val headerMap = hashMapOf<String, String>()
        headerMap["token"] = "5255646464"
        headerMap["school"] = "1007"
        Api.api.baseUrl = "http://wthrcdn.etouch.cn/"
        Api.api.isDebug = BuildConfig.DEBUG
        Api.api.headerMap = headerMap

        Api.api.getApi(ApiObserver::class.java)
            .getWeather("101180301")
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(BaseRspObserver(WeatherResponse::class.java, Action1 {
                tv_weather.text = Gson().toJson(it)
            }))
    }

}
