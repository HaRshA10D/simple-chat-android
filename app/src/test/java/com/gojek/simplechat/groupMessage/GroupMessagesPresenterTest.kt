package com.gojek.simplechat.groupMessage

import com.gojek.simplechat.RxSchedulersOverrideRule
import com.gojek.simplechat.groupMessage.model.GroupMessage
import com.gojek.simplechat.groupMessage.model.GroupMessages
import com.gojek.simplechat.groupMessage.model.GroupMessagesResponse
import com.gojek.simplechat.api.SimpleChatApi
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
        Mockito.`when`(simpleChatApi.getLatestMessages("1")).thenReturn(Single.just(Response.success(groupMessagesResponse)))
        groupMessagesPresenter.groupMessages("1")
        verify(groupMessagesView).populateGroupMessages(groupMessagesResponse)
    }

    @Test
    fun groupMessagesFailedResponseDueToServer() {
        val groupMessagesResponse = "{\"data\":{\"message\":\"Unauthorised user\" } }"
        Mockito.`when`(simpleChatApi.getLatestMessages("1"))
                .thenReturn(Single.just(Response.error(400, ResponseBody.create(MediaType.parse("application/json"), groupMessagesResponse))))
        groupMessagesPresenter.groupMessages("1")
        verify(groupMessagesView).showCustomError("Unauthorised user")
    }

    @Test
    fun groupMessagesFailedResponseDueToWrongContractSentByServer() {
        val groupMessagesResponse = "{\"NoData\":{\"message\":\"Unauthorised user\" } }"
        Mockito.`when`(simpleChatApi.getLatestMessages("1"))
                .thenReturn(Single.just(Response.error(400, ResponseBody.create(MediaType.parse("application/json"), groupMessagesResponse))))
        groupMessagesPresenter.groupMessages("1")
        verify(groupMessagesView).showCustomError("Something Went Wrong")
    }

    @Test
    fun networkError() {
        Mockito.`when`(simpleChatApi.getLatestMessages("1")).thenReturn(Single.error(IOException()))
        groupMessagesPresenter.groupMessages("1")
        verify(groupMessagesView).showNetworkError()
    }
}
