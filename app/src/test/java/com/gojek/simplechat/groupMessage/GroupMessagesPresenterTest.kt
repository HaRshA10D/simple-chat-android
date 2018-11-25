package com.gojek.simplechat.groupMessage

import com.gojek.simplechat.RxSchedulersOverrideRule
import com.gojek.simplechat.api.SimpleChatApi
import com.gojek.simplechat.groupMessage.model.*
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import retrofit2.Response
import java.io.IOException

class GroupMessagesPresenterTest {

    @Rule
    @JvmField
    val overrideSchedulerRule = RxSchedulersOverrideRule()

    @Mock
    private lateinit var groupMessagesView: GroupMessagesView
    private lateinit var groupMessagesPresenter: GroupMessagesPresenter
    private lateinit var simpleChatApi: SimpleChatApi

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        groupMessagesView = mock(GroupMessagesView::class.java)
        groupMessagesPresenter = GroupMessagesPresenter(groupMessagesView)
        simpleChatApi = mock(SimpleChatApi::class.java)
        groupMessagesPresenter.simpleChatApi = simpleChatApi
    }

    @Test
    fun groupMessagesReceivedSuccessfully() {
        val groupMessage = GroupMessage("test", "test", "1111")
        val groupMessages = GroupMessages(listOf(groupMessage), "error")
        val groupMessagesResponse = GroupMessagesResponse(groupMessages)
        val userToken = "mockUserToken"
        Mockito.`when`(simpleChatApi.getLatestMessages("1", userToken)).thenReturn(Single.just(Response.success(groupMessagesResponse)))
        groupMessagesPresenter.groupMessages("1", userToken)
        verify(groupMessagesView).populateGroupMessages(groupMessagesResponse)
    }

    @Test
    fun groupMessagesFailedResponseDueToServer() {
        val groupMessagesResponse = "{\"data\":{\"message\":\"Unauthorised user\" } }"
        val userToken = "mockUserToken"
        Mockito.`when`(simpleChatApi.getLatestMessages("1", userToken))
                .thenReturn(Single.just(Response.error(400, ResponseBody.create(MediaType.parse("application/json"), groupMessagesResponse))))
        groupMessagesPresenter.groupMessages("1", userToken)
        verify(groupMessagesView).showCustomError("Unauthorised user")
    }

    @Test
    fun groupMessagesFailedResponseDueToWrongContractSentByServer() {
        val groupMessagesResponse = "{\"NoData\":{\"message\":\"Unauthorised user\" } }"
        val userToken = "mockUserToken"
        Mockito.`when`(simpleChatApi.getLatestMessages("1", userToken))
                .thenReturn(Single.just(Response.error(400, ResponseBody.create(MediaType.parse("application/json"), groupMessagesResponse))))
        groupMessagesPresenter.groupMessages("1", userToken)
        verify(groupMessagesView).showCustomError("Something Went Wrong")
    }

    @Test
    fun networkError() {
        val userToken = "mockUserToken"
        Mockito.`when`(simpleChatApi.getLatestMessages("1", userToken)).thenReturn(Single.error(IOException()))
        groupMessagesPresenter.groupMessages("1", userToken)
        verify(groupMessagesView).showNetworkError()
    }

    @Test
    fun sendMessagesSuccessful() {
        val sendMessageRequestBody = SendMessageRequestBody("Hey how are you","15431653420000")
        val sendMessageResponseBody = SendMessageResponseBody(SendMessageResponse("Successfully created message"))
        val userToken = "mockUserToken"
        Mockito.`when`(simpleChatApi.sendMessage("1", userToken,sendMessageRequestBody)).thenReturn(Single.just(sendMessageResponseBody))
        groupMessagesPresenter.sendMessageButtonClicked(userToken,"1","Hey how are you", "15431653420000")
        verify(groupMessagesView).onSendMessageSuccessful()
    }

    @Test
    fun sendMessagesFailed() {
        val sendMessageRequestBody = SendMessageRequestBody("Hey how are you","15431653420000")
        val sendMessageResponseBody = SendMessageResponseBody(SendMessageResponse("Successfully created message"))
        val userToken = "mockUserToken"
        Mockito.`when`(simpleChatApi.sendMessage("1", userToken,sendMessageRequestBody)).thenReturn(Single.error(IOException()))
        groupMessagesPresenter.sendMessageButtonClicked(userToken,"1","Hey how are you", "15431653420000")
        verify(groupMessagesView).onSendMessageFailed()
    }
}
