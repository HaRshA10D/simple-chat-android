package com.gojek.simplechat.userGroup.model

import com.google.gson.annotations.SerializedName

data class CreateGroupResponseBody(
        @SerializedName("data") val data: Group
)
