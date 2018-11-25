package com.gojek.simplechat.userGroup

import com.gojek.simplechat.RxSchedulersOverrideRule
import com.gojek.simplechat.api.SimpleChatApi
import com.gojek.simplechat.userGroup.model.*
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import retrofit2.Response
import java.io.IOException

class UserGroupPresenterTest {

    @Rule
    @JvmField
    var overrideSchedulerRule = RxSchedulersOverrideRule()

    @Mock
    private lateinit var simpleChatApi: SimpleChatApi
    private lateinit var userGroupView: UserGroupView
    private lateinit var userGroupPresenter: UserGroupPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        userGroupView = mock(UserGroupView::class.java)
        userGroupPresenter = UserGroupPresenter(userGroupView)
        userGroupPresenter.simpleChatApi = simpleChatApi
    }

    @Test
    fun userGroupFetchedSucessfully() {
        val response = UserGroupResponse(UserGroupList(mutableListOf(
                UserGroup("1", "Harsha"),
                UserGroup("2", "Juan")
        ), null))
        val mockUserToken = "123123-123123-123123"

        `when`(simpleChatApi.getUserGroups(mockUserToken)).thenReturn(Single.just(Response.success(response)))
        userGroupPresenter.populateUserGroups(mockUserToken)
        Mockito.verify(userGroupView).onGetUserGroupSuccessFetch(response)
    }

    @Test
    fun userGroupFailToFetchDueToNetworkError() {
        val mockUserToken = "123123-123123-123123"
        `when`(simpleChatApi.getUserGroups(mockUserToken)).thenReturn(Single.create { it.onError(IOException()) })
        userGroupPresenter.populateUserGroups(mockUserToken)
        Mockito.verify(userGroupView).onGetUserGroupFailedFetch()
    }

    @Test
    fun navigateToGroupChatWithGroupData() {
        userGroupPresenter.groupCardClicked("1", "ops-tech")
        Mockito.verify(userGroupView).navigateToGroupChatUI("1", "ops-tech")
    }

    @Test
    fun groupNameCannotBeEmpty() {
        userGroupPresenter.createGroupButtonClicked("123", "")
        Mockito.verify(userGroupView).groupNameIsEmptyMessage()
    }

    @Test
    fun groupCreatedSucessfully() {
        val request = CreateGroupRequestBody(
                "ops-tech"
        )
        val response = CreateGroupResponseBody(
                Group("1", "ops-tech")
        )
        val mockUserToken = "123123-123123-123123"
        val mockUserGroupName = "ops-tech"
        `when`(simpleChatApi.createGroup(mockUserToken, request)).thenReturn(Single.just((response)))
        userGroupPresenter.createGroupButtonClicked(mockUserToken, mockUserGroupName)
        Mockito.verify(userGroupView).onCreateGroupSuccess(response)
    }

    @Test
    fun groupCreationFailed() {
        val request = CreateGroupRequestBody(
                "ops-tech"
        )
        val mockUserToken = "123123-123123-123123"
        val mockUserGroupName = "ops-tech"
        `when`(simpleChatApi.createGroup(mockUserToken, request)).thenReturn(Single.error(IOException()))
        userGroupPresenter.createGroupButtonClicked(mockUserToken, mockUserGroupName)
        Mockito.verify(userGroupView).onCreateGroupFailed()
    }
}
