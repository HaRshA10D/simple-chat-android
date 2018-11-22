package com.gojek.simplechat.groupMessage

import com.gojek.simplechat.groupMessage.model.GroupMessagesResponse

interface GroupMessagesView {
    fun populateGroupMessages(groupMessagesResponse: GroupMessagesResponse)
    fun showNetworkError()
    fun showCustomError(errorMessage: String?)
}
