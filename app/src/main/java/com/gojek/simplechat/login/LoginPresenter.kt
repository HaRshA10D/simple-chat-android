package com.gojek.simplechat.login

import android.annotation.SuppressLint
import com.gojek.simplechat.api.SimpleChatApi
import com.gojek.simplechat.login.model.LoginRequestBody
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class LoginPresenter(private val loginView: LoginView) {

    @Inject
    lateinit var simpleChatApi: SimpleChatApi

    fun isNameValid(name: String): Boolean {
        return name.matches(Regex("[a-zA-Z0-9]+"))
    }

    @SuppressLint("CheckResult")
    fun loginUser(name: String) {
        simpleChatApi.callLoginApi(LoginRequestBody(name))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> loginView.goToHome(result.responseData.userToken, result.responseData.responseMessage) },
                        { loginView.loginFailed() }
                )
    }
}
