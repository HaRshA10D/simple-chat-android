package com.gojek.simplechat.groupMessage

import android.annotation.SuppressLint
import com.gojek.simplechat.api.SimpleChatApi
import com.gojek.simplechat.groupMessage.model.SendMessageRequestBody
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GroupMessagesPresenter(private val groupMessagesView: GroupMessagesView) {

    @Inject
    lateinit var simpleChatApi: SimpleChatApi

    @SuppressLint("CheckResult")
    fun groupMessages(id: String, userToken: String) {
        simpleChatApi.getLatestMessages(id, userToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            val groupMessageResponse = GroupMessageResponse(it)
                            if (groupMessageResponse.isSuccess()) {
                                groupMessagesView.populateGroupMessages(groupMessageResponse.messages())
                            } else {
                                groupMessagesView.showCustomError(groupMessageResponse.errorMessage())
                            }

                        },
                        { groupMessagesView.showNetworkError() }
                )
    }

    @SuppressLint("CheckResult")
    fun sendMessageButtonClicked(userToken: String, groupId: String, textMessage: String, sendMessageTime: String) {
        simpleChatApi.sendMessage(groupId, userToken, SendMessageRequestBody(textMessage, sendMessageTime))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            groupMessagesView.onSendMessageSuccessful()
                        },
                        {
                            groupMessagesView.onSendMessageFailed()
                        }
                )
    }
}
