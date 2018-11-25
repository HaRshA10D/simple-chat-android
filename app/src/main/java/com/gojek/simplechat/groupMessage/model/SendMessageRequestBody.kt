package com.gojek.simplechat.groupMessage.model

import com.google.gson.annotations.SerializedName

data class SendMessageRequestBody(@SerializedName("text") val textMessage: String,
                                  @SerializedName("message_sent_time") val messageSentTime: String)
