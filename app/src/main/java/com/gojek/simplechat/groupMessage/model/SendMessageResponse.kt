package com.gojek.simplechat.groupMessage.model

import com.google.gson.annotations.SerializedName

data class SendMessageResponse(@SerializedName("message") val message: String)
