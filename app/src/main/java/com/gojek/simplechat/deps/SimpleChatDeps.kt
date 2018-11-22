package com.gojek.simplechat.deps

import com.gojek.simplechat.login.LoginPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface SimpleChatDeps {
    fun inject(loginPresenter: LoginPresenter)
}
