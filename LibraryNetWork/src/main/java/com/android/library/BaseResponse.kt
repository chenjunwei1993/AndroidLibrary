package com.android.library

import java.io.Serializable

open class BaseResponse : Serializable {
    var error_code: Int = 0
    var error_msg: String = ""

    constructor()

    constructor(error_code: Int, error_msg: String) {
        this.error_code = error_code
        this.error_msg = error_msg
    }

    override fun toString(): String {
        return "error_code:$error_code,error_msg:$error_msg"
    }
}