package com.android.library

import android.util.Log
import org.json.JSONException
import retrofit2.adapter.rxjava.HttpException
import rx.Subscriber
import rx.functions.Action1
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLProtocolException

open class BaseRspObserver<T>(private var entityClass: Class<T>, private val action: Action1<T>?) : Subscriber<T>() {

    override fun onCompleted() {}

    override fun onError(e: Throwable) {
        Log.e(TAG, "onError:$e")
        e.printStackTrace()
        val code: Int
        var msg = ""
        if (e is ConnectException) {
            code = ErrorCode.CONNECTION_EXCEPTION
        } else if (e is HttpException) {
            code = e.code()
            msg = e.message()
        } else if (e is SocketTimeoutException) {
            code = ErrorCode.SOCKET_TIMEOUT_EXCEPTION
        } else if (e is SSLHandshakeException || e is SSLProtocolException) {
            code = ErrorCode.SSL_HANDSHAKE_EXCEPTION
        } else if (e is JSONException) {
            code = ErrorCode.JSON_EXCEPTION
        } else {
            code = ErrorCode.OTHER_EXCEPTION
        }
        try {
            val t = entityClass.newInstance()
            if (t is BaseResponse) {
                val rsp = t as BaseResponse
                rsp.error_code = code
                rsp.error_msg = msg
                onNext(t)
            }
        } catch (e1: Exception) {
            Log.e(TAG, "onError:$e1")
            e1.printStackTrace()
        }

    }

    override fun onNext(t: T) {
        Log.d(TAG, "onNext")
        action?.call(t)
    }

    companion object {
        private val TAG = BaseRspObserver::class.java.simpleName
    }
}