package com.gojek.simplechat.api

import com.gojek.simplechat.constants.Constant
import com.gojek.simplechat.groupMessage.model.GroupMessagesResponse
import com.gojek.simplechat.login.model.LoginRequestBody
import com.gojek.simplechat.login.model.LoginResponseBody
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SimpleChatApi {

    @POST(Constant.LOGIN_API_PATH)
    fun callLoginApi(@Body loginRequestBody: LoginRequestBody): Single<LoginResponseBody>

    @GET("groups/{id}/messages?page=1&per_page=10")
    fun getLatestMessages(@Path("id") groupId: String): Single<Response<GroupMessagesResponse>>
}
