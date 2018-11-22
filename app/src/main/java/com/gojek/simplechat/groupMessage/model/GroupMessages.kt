package com.gojek.simplechat.groupMessage.model

import com.google.gson.annotations.SerializedName

data class GroupMessages(
        @SerializedName("messages") val groupMessageList: List<GroupMessage>,
        @SerializedName("message") val errorMessage: String?
)
