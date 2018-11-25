package com.gojek.simplechat.groupMessage.model

import com.google.gson.annotations.SerializedName

data class SendMessageResponseBody(@SerializedName("data") val data: SendMessageResponse)
