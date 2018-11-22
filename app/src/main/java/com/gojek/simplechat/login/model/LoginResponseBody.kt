package com.gojek.simplechat.login.model

import com.google.gson.annotations.SerializedName

data class LoginResponseBody(@SerializedName("data") val responseData: ResponseData)
