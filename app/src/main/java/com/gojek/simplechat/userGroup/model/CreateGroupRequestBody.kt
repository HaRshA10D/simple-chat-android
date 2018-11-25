package com.gojek.simplechat.userGroup.model

import com.google.gson.annotations.SerializedName

data class CreateGroupRequestBody(@SerializedName("name") val name: String)
