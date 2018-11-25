package com.gojek.simplechat.userGroup.model

import com.google.gson.annotations.SerializedName

data class UserGroupList (
        @SerializedName("groups") val userGroupList: MutableList<UserGroup>,
        @SerializedName("message") val errorMessage: String?
)
