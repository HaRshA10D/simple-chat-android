package com.gojek.simplechat.api

import com.gojek.simplechat.constants.Constant
import com.gojek.simplechat.groupMessage.model.GroupMessagesResponse
import com.gojek.simplechat.login.model.LoginRequestBody
import com.gojek.simplechat.login.model.LoginResponseBody
import com.gojek.simplechat.userGroup.model.CreateGroupRequestBody
import com.gojek.simplechat.userGroup.model.CreateGroupResponseBody
import com.gojek.simplechat.userGroup.model.UserGroupResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface SimpleChatApi {

    @POST(Constant.LOGIN_API_PATH)
    fun callLoginApi(@Body loginRequestBody: LoginRequestBody): Single<LoginResponseBody>

    @GET("groups/{id}/messages?page=1&per_page=10")
    fun getLatestMessages(@Path("id") groupId: String): Single<Response<GroupMessagesResponse>>

    @GET("/groups")
    fun getUserGroups(@Header("Auth-Token") token: String): Single<Response<UserGroupResponse>>

    @POST("/groups")
    fun createGroup(@Header("Auth-Token") token: String, @Body createGroupRequestBody: CreateGroupRequestBody): Single<CreateGroupResponseBody>
}
