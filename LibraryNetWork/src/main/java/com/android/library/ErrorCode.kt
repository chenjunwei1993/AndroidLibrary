package com.android.library

/**
*@author chenjunwei
*@desc 错误码
*@date 2019/8/26
*/
object ErrorCode {
    const val SUCCESS = 1000
    const val OTHER_EXCEPTION = -99
    const val CONNECTION_EXCEPTION = -100
    const val SOCKET_TIMEOUT_EXCEPTION = -101
    const val SSL_HANDSHAKE_EXCEPTION = -102
    const val JSON_EXCEPTION = -103

    fun isSuccess(code : Int) : Boolean{
        return code == SUCCESS
    }
}