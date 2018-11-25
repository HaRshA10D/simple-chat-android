package com.gojek.simplechat.userGroup.model

import com.google.gson.annotations.SerializedName

data class JoinGroupResponseBody(
        @SerializedName("data") val data: Group
)
