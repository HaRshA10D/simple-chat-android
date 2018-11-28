package com.gojek.simplechat.constants

object Constant {
    const val BASE_URL = "http://10.0.2.2:3000"
    const val LOGIN_API_PATH = "/users/login"
    const val USER_TOKEN = "USER_TOKEN"
    const val USER_TOKEN_DEFAULT = ""
    const val SIMPLE_CHAT_SHARED_PREF = "SIMPLE_CHAT_SHARED_PREF"
    const val UNAUTHORIZED_ACCESS = "Unauthorized Access"
    const val GROUP_DOES_NOT_EXIST = "Group does not exist"
    const val YOU_ALREADY_JOIN_THE_GROUP = "You already joined this group"
    const val UNEXPECTED_ERROR = "Unexpected Error"
    const val IO_EXCEPTION = "IO Exception"
    const val GROUP_ID = "group_id"
    const val GROUP_NAME = "group_name"
    const val GROUP_NAME_REGEX_MATCHER = "^\\w+\$"
    const val TIMEOUT: Long = 15
    const val HUNDRED_MILLISECOND = 100L
}
