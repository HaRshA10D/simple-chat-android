package com.gojek.simplechat.login

import com.gojek.simplechat.RxSchedulersOverrideRule
import com.gojek.simplechat.api.SimpleChatApi
import com.gojek.simplechat.login.model.LoginRequestBody
import com.gojek.simplechat.login.model.LoginResponseBody
import com.gojek.simplechat.login.model.ResponseData
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.io.IOException

class LoginPresenterTest {

    @Rule
    @JvmField
    val overrideSchedulerRule = RxSchedulersOverrideRule()

    @Test
    fun inputNameIsInvalid() {
        val loginView = mock(LoginView::class.java)
        val loginPresenter = LoginPresenter(loginView)
        val emptyName = ""
        val specialCharacterName = "Umesh_ghsvdgvs"
        val specialCharacterAtStartName = "$12435363"

        val emptyNameIsInvalid = loginPresenter.isNameValid(emptyName)
        val specialCharacterNameIsInvalid = loginPresenter.isNameValid(specialCharacterName)
        val specialCharacterAtStartNameIsInvalid = loginPresenter.isNameValid(specialCharacterAtStartName)

        assertEquals(false, emptyNameIsInvalid)
        assertEquals(false, specialCharacterNameIsInvalid)
        assertEquals(false, specialCharacterAtStartNameIsInvalid)
    }

    @Test
    fun inputNameIsValid() {
        val loginView = mock(LoginView::class.java)
        val loginPresenter = LoginPresenter(loginView)
        val nameWithOnlyAlphabets = "Umesh"
        val nameWithOnlyNumbers = "125364563"
        val nameWithAlphaNumeric = "Umesh1462536"

        val nameWithOnlyAlphabetsIsValid = loginPresenter.isNameValid(nameWithOnlyAlphabets)
        val nameWithOnlyNumbersIsValid = loginPresenter.isNameValid(nameWithOnlyNumbers)
        val nameWithAlphaNumericIsValid = loginPresenter.isNameValid(nameWithAlphaNumeric)

        assertEquals(true, nameWithOnlyAlphabetsIsValid)
        assertEquals(true, nameWithOnlyNumbersIsValid)
        assertEquals(true, nameWithAlphaNumericIsValid)
    }

    @Test
    fun loginSuccessful() {
        val chatApi = mock(SimpleChatApi::class.java)
        val loginView = mock(LoginView::class.java)
        val loginPresenter = LoginPresenter(loginView)
        loginPresenter.simpleChatApi = chatApi
        val responseBody = LoginResponseBody(ResponseData("1234", "Login Successful"))
        val name = "ravi"

        Mockito.`when`(chatApi.callLoginApi(LoginRequestBody("ravi"))).thenReturn(Single.just(responseBody))

        loginPresenter.loginUser(name)
        verify(loginView).goToHome(responseBody.responseData.userToken, responseBody.responseData.responseMessage)
    }

    @Test
    fun loginUnsuccessful() {
        val chatApi = mock(SimpleChatApi::class.java)
        val loginView = mock(LoginView::class.java)
        val loginPresenter = LoginPresenter(loginView)
        loginPresenter.simpleChatApi = chatApi
        val name = ""
        Mockito.`when`(chatApi.callLoginApi(LoginRequestBody(name))).thenReturn(Single.error(IOException()))

        loginPresenter.loginUser(name)
        verify(loginView).loginFailed()
    }
}
