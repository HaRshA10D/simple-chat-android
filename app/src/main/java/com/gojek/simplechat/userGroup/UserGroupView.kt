package com.gojek.simplechat.userGroup

import com.gojek.simplechat.userGroup.model.CreateGroupResponseBody
import com.gojek.simplechat.userGroup.model.JoinGroupResponseBody
import com.gojek.simplechat.userGroup.model.UserGroupResponse

interface UserGroupView {
    fun onGetUserGroupSuccessFetch(userGroupResponse: UserGroupResponse)
    fun onGetUserGroupFailedFetch()
    fun setGroupCardClickListener(groupId: String, groupName: String)
    fun showAlert(status: String)
    fun onCreateGroupSuccess(createGroupResponseBody: CreateGroupResponseBody)
    fun onCreateGroupFailed()
    fun groupNameIsEmptyMessage()
    fun groupNameIsNotAlphanumeric()
    fun createUserGroup(userGroupName: String)
    fun navigateToGroupChatUI(groupId: String, groupName: String)
    fun onJoinGroupSuccess(joinGroupResponseBody: JoinGroupResponseBody)
    fun onJoinGroupFailed(errorMessage: String)
}
