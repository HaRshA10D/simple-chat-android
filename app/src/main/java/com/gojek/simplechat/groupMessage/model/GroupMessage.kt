package com.gojek.simplechat.groupMessage.model

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*


data class GroupMessage(
        @SerializedName("user_name") val userName: String,
        @SerializedName("text") val messageText: String,
        @SerializedName("timestamp") val messageTimestamp: String
){
    fun epochToDateTime() : String {
        val timestamp = messageTimestamp.toLong()
        val date = Date(timestamp)
        val format = SimpleDateFormat("dd/MM H:mm", Locale.US)
        format.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        return format.format(date)
    }
}
