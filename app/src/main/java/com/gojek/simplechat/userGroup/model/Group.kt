package com.gojek.simplechat.userGroup.model

import com.google.gson.annotations.SerializedName

data class Group(
        @SerializedName("group_id") val id: String,
        @SerializedName("group_name") val name: String
)
