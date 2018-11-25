package com.gojek.simplechat.userGroup

import android.annotation.SuppressLint
import com.gojek.simplechat.api.SimpleChatApi
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

    fun groupCardClicked(groupId: String, groupName: String) {
        userGroupView.navigateToGroupChatUI(groupId, groupName)
    }
}
