package com.gojek.simplechat.error.model

import com.gojek.simplechat.userGroup.model.Group
import com.google.gson.annotations.SerializedName

data class ErrorResponse(
        @SerializedName("data") val data: Group
)
