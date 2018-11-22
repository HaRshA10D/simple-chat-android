package com.gojek.simplechat.login

interface LoginView {
    fun loginFailed()
    fun goToHome(token: String, responseMessage: String)
}
