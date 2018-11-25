package com.gojek.simplechat.userGroup

import android.annotation.SuppressLint
import com.gojek.simplechat.api.SimpleChatApi
import com.gojek.simplechat.error.JoinGroupHttpError
import com.gojek.simplechat.userGroup.model.CreateGroupRequestBody
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserGroupPresenter(private val userGroupView: UserGroupView) {

    @Inject
    lateinit var simpleChatApi: SimpleChatApi

    @SuppressLint("CheckResult")
    fun populateUserGroups(userToken: String) {
        simpleChatApi.getUserGroups(userToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    userGroupView.onGetUserGroupSuccessFetch(it.body()!!)
                }, {
                    userGroupView.onGetUserGroupFailedFetch()
                })
    }

    @SuppressLint("CheckResult")
    fun createGroupButtonClicked(userToken: String, groupName: String) {

        if (groupName.isEmpty()) {
            userGroupView.groupNameIsEmptyMessage()
        } else {
            simpleChatApi.createGroup(userToken, CreateGroupRequestBody(groupName))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        userGroupView.onCreateGroupSuccess(result)
                    }, {
                        userGroupView.onCreateGroupFailed()
                    })
        }
    }

    fun groupCardClicked(groupId: String, groupName: String) {
        userGroupView.navigateToGroupChatUI(groupId, groupName)
    }

    @SuppressLint("CheckResult")
    fun joinGroupButtonClicked(userToken: String, groupName: String) {

        if (groupName.isEmpty()) {
            userGroupView.groupNameIsEmptyMessage()
        } else {
            simpleChatApi.joinGroup(userToken, groupName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        userGroupView.onJoinGroupSuccess(result)
                    }, { error ->
                        val errorMessage = JoinGroupHttpError(error).getErrorResponseMessage()
                        userGroupView.onJoinGroupFailed(errorMessage)
                    })
        }
    }
}
