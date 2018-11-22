package com.gojek.simplechat.login.model

import com.google.gson.annotations.SerializedName

data class ResponseData(@SerializedName("token") val userToken: String,
                        @SerializedName("message") val responseMessage: String)
