package com.chenjunwei.library.network

import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable


/**
 * @author chenjunwei
 * @desc
 * @date 2019/7/4
 */
interface ApiObserver {
    /**
     * 天气
     *
     * @param citykey
     * @return
     */
    @GET("weather_mini")
    fun getWeather(@Query("citykey") citykey: String): Observable<WeatherResponse>
}
