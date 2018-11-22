package com.gojek.simplechat.groupMessage

import com.gojek.simplechat.api.SimpleChatApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GroupMessagesPresenter(private val groupMessagesView: GroupMessagesView) {

    @Inject
    lateinit var simpleChatApi: SimpleChatApi

    fun groupMessages(id: String) {
        simpleChatApi.getLatestMessages(id)
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
}
