package com.gojek.simplechat.deps

import com.gojek.simplechat.groupMessage.GroupMessageResponse
import com.gojek.simplechat.groupMessage.GroupMessagesPresenter
import dagger.Component

@Component(modules = [NetworkModule::class])
interface SimpleChatDeps {
    fun inject(groupMessagesPresenter: GroupMessagesPresenter)
    fun inject(groupMessageResponse: GroupMessageResponse)
}
