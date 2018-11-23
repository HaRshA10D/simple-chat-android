package com.gojek.simplechat.deps

import com.gojek.simplechat.userGroup.UserGroupPresenter
import com.gojek.simplechat.groupMessage.GroupMessageResponse
import com.gojek.simplechat.groupMessage.GroupMessagesPresenter
import com.gojek.simplechat.login.LoginPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface SimpleChatDeps {
    fun inject(groupMessagesPresenter: GroupMessagesPresenter)
    fun inject(groupMessageResponse: GroupMessageResponse)
    fun inject(userGroupPresenter: UserGroupPresenter)
    fun inject(loginPresenter: LoginPresenter)
}
