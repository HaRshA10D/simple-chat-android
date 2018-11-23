package com.gojek.simplechat.userGroup.model

import com.google.gson.annotations.SerializedName

data class UserGroupList (
        @SerializedName("groups") val userGroupList: List<UserGroup>,
        @SerializedName("message") val errorMessage: String?
)
