package com.gojek.simplechat.login.model

import com.google.gson.annotations.SerializedName

data class LoginRequestBody(@SerializedName("name") val userName: String)
