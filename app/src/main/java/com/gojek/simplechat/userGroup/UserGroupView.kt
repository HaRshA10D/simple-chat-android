package com.gojek.simplechat.userGroup

import com.gojek.simplechat.userGroup.model.CreateGroupResponseBody
import com.gojek.simplechat.userGroup.model.UserGroupResponse

interface UserGroupView {
    fun onGetUserGroupSuccessFetch(userGroupResponse: UserGroupResponse)
    fun onGetUserGroupFailedFetch()
    fun setGroupCardClickListener(groupId: String, groupName: String)
    fun showAlert(status: String)
    fun onCreateGroupSuccess(createGroupResponseBody: CreateGroupResponseBody)
    fun onCreateGroupFailed()
    fun groupNameIsEmptyMessage()
    fun navigateToGroupChatUI(groupId: String, groupName: String)
}
