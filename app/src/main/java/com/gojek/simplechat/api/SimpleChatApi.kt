package com.gojek.simplechat.api

import com.gojek.simplechat.groupMessage.model.GroupMessagesResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SimpleChatApi {
    @GET("groups/{id}/messages?page=1&per_page=10")
    fun getLatestMessages(@Path("id") groupId: String): Single<Response<GroupMessagesResponse>>
}
