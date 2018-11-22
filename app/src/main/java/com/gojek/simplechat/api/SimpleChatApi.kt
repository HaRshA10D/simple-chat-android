package com.gojek.simplechat.api

import com.gojek.simplechat.constants.Constant
import com.gojek.simplechat.login.model.LoginRequestBody
import com.gojek.simplechat.login.model.LoginResponseBody
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface SimpleChatApi {

    @POST(Constant.LOGIN_API_PATH)
    fun callLoginApi(@Body loginRequestBody: LoginRequestBody): Single<LoginResponseBody>
}
