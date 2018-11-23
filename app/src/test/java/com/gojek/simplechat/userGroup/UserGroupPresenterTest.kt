package com.gojek.simplechat.userGroup

import com.gojek.simplechat.RxSchedulersOverrideRule
import com.gojek.simplechat.api.SimpleChatApi
import com.gojek.simplechat.userGroup.model.UserGroup
import com.gojek.simplechat.userGroup.model.UserGroupList
import com.gojek.simplechat.userGroup.model.UserGroupResponse
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
        val response = UserGroupResponse(UserGroupList(listOf(
                UserGroup("1", "Harsha"),
                UserGroup("2", "Juan")
        ), null))

        `when`(simpleChatApi.getUserGroups()).thenReturn(Single.just(Response.success(response)))
        userGroupPresenter.populateUserGroups()
        Mockito.verify(userGroupView).onGetUserGroupSuccessFetch(response)
    }

    @Test
    fun userGroupFailToFetchDueToNetworkError() {
        `when`(simpleChatApi.getUserGroups()).thenReturn(Single.create { it.onError(IOException()) })
        userGroupPresenter.populateUserGroups()
        Mockito.verify(userGroupView).onGetUserGroupFailedFetch()
    }

    @Test
    fun navigateToGroupChatWithGroupData() {
        userGroupPresenter.groupCardClicked("1", "ops-tech")
        Mockito.verify(userGroupView).navigateToGroupChatUI("1", "ops-tech")
    }
}
