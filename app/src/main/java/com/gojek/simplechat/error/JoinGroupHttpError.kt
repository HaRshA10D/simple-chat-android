package com.gojek.simplechat.error

import com.gojek.simplechat.constants.Constant
import retrofit2.HttpException

class JoinGroupHttpError(private val throwable: Throwable) {

    fun getErrorResponseMessage(): String {
        if (throwable is HttpException) {
            val exception = throwable
            when (exception.code()) {
                401 -> {
                    return Constant.UNAUTHORIZED_ACCESS
                }
                404 -> {
                    return Constant.GROUP_DOES_NOT_EXIST
                }
                409 -> {
                    return Constant.YOU_ALREADY_JOIN_THE_GROUP
                }
                else -> {
                    return Constant.UNEXPECTED_ERROR
                }
            }
        } else {
            return Constant.IO_EXCEPTION
        }
    }
}
