package com.gojek.simplechat.groupMessage

import com.gojek.simplechat.deps.DaggerSimpleChatDeps
import com.gojek.simplechat.groupMessage.model.GroupMessages
import com.gojek.simplechat.groupMessage.model.GroupMessagesResponse
import com.google.gson.Gson
import retrofit2.Response
import javax.inject.Inject

class GroupMessageResponse(response: Response<GroupMessagesResponse>) {

    private var groupMessageResponse: Response<GroupMessagesResponse> = response

    init {
        val daggerGroupMessageResponse = DaggerSimpleChatDeps.builder().build()
        daggerGroupMessageResponse.inject(this)
    }

    @Inject
    lateinit var gson: Gson

    fun isSuccess(): Boolean {
        return groupMessageResponse.isSuccessful
    }

    fun messages(): GroupMessagesResponse {
        return groupMessageResponse.body()!!
    }

    fun errorMessage(): String? {
        val errorJson = groupMessageResponse.errorBody()?.string()
        val errorMessage: GroupMessages? = gson.fromJson(errorJson, GroupMessagesResponse::class.java).data
        if (errorMessage != null) {
            return errorMessage.errorMessage
        }
        return "Something Went Wrong"
    }
}
