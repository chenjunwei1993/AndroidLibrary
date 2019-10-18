package com.android.library

import android.util.Log
import okhttp3.*
import okio.Buffer
import java.io.IOException

/**
 *@author chenjunwei
 *@desc 接口拦截器
 *@date 2019-08-28
 */
class HttpLogInterceptor : Interceptor {

    private val F_BREAK = " %n"
    private val F_URL = " %s"
    private val F_TIME = " in %.1fms"
    private val F_HEADERS = "%s"
    private val F_RESPONSE = F_BREAK + "Response: %d"
    private val F_BODY = "body: %s"

    private val F_BREAKER = "$F_BREAK-------------------------------------------$F_BREAK"
    private val F_REQUEST_WITHOUT_BODY = F_URL + F_TIME + F_BREAK + F_HEADERS
    private val F_RESPONSE_WITHOUT_BODY = F_RESPONSE + F_BREAK + F_HEADERS + F_BREAKER
    private val F_REQUEST_WITH_BODY = F_URL + F_TIME + F_BREAK + F_HEADERS + F_BODY + F_BREAK
    private val F_RESPONSE_WITH_BODY = F_RESPONSE + F_BREAK + F_HEADERS + F_BODY + F_BREAK + F_BREAKER

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val t1 = System.nanoTime()
        val response = chain.proceed(request)
        val t2 = System.nanoTime()

        var contentType: MediaType? = response.body()?.contentType()
        var bodyString: String? = response.body()?.string()

        val time = (t2 - t1) / 1e6

        when (request.method()) {
            "GET" -> Log.d(
                "retrofit-->", String.format(
                    "GET $F_REQUEST_WITHOUT_BODY$F_RESPONSE_WITH_BODY",
                    request.url(),
                    time,
                    request.headers(),
                    response.code(),
                    response.headers(),
                    stringifyResponseBody(bodyString)
                )
            )
            "POST" -> Log.d(
                "retrofit-->", String.format(
                    "POST $F_REQUEST_WITH_BODY$F_RESPONSE_WITH_BODY",
                    request.url(),
                    time,
                    request.headers(),
                    stringifyRequestBody(request),
                    response.code(),
                    response.headers(),
                    stringifyResponseBody(bodyString)
                )
            )
            "PUT" -> Log.d(
                "retrofit-->", String.format(
                    "PUT $F_REQUEST_WITH_BODY$F_RESPONSE_WITH_BODY",
                    request.url(),
                    time,
                    request.headers(),
                    request.body().toString(),
                    response.code(),
                    response.headers(),
                    stringifyResponseBody(bodyString)
                )
            )
            "DELETE" -> Log.d(
                "retrofit-->", String.format(
                    "DELETE $F_REQUEST_WITHOUT_BODY$F_RESPONSE_WITHOUT_BODY",
                    request.url(),
                    time,
                    request.headers(),
                    response.code(),
                    response.headers()
                )
            )
        }

        return if (response.body() != null) {
            val body = ResponseBody.create(contentType, bodyString)
            response.newBuilder().body(body).build()
        } else {
            response
        }
    }


    private fun stringifyRequestBody(request: Request): String {
        return try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body()?.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            "did not work"
        }

    }

    private fun stringifyResponseBody(responseBody: String?): String? {
        return responseBody
    }
}