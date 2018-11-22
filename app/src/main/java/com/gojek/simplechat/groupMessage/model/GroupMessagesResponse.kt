package com.gojek.simplechat.groupMessage.model

import com.google.gson.annotations.SerializedName

data class GroupMessagesResponse(
        @SerializedName("data") val data: GroupMessages
)
