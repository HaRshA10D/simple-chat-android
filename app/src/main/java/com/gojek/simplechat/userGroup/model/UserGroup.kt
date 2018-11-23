package com.gojek.simplechat.userGroup.model

import com.google.gson.annotations.SerializedName

data class UserGroup(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String
)
