package com.chenjunwei.library.network

import com.google.gson.annotations.SerializedName


/**
 * @author chenjunwei
 * @desc 首页信息
 * @date 2019/6/18
 */
class WeatherResponse {
    @SerializedName("status")
    var status: String? = null

    @SerializedName("desc")
    var desc: String? = null

    @SerializedName("data")
    var data: Data? = null

    inner class Data
}
