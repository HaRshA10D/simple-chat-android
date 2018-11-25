package com.gojek.simplechat.userGroup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.gojek.simplechat.R
import com.gojek.simplechat.constants.Constant
import com.gojek.simplechat.datastore.SharedPreferenceModule
import com.gojek.simplechat.deps.DaggerSimpleChatDeps
import com.gojek.simplechat.groupMessage.GroupMessagesActivity
import com.gojek.simplechat.userGroup.adapter.UserGroupAdapter
import com.gojek.simplechat.userGroup.model.*

class UserGroupActivity : AppCompatActivity(), UserGroupView {

    private lateinit var userGroupRecyclerView: RecyclerView
    private lateinit var userGroupPresenter: UserGroupPresenter
    private lateinit var userToken: String
    private lateinit var userGroupList: MutableList<UserGroup>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_group)

        userGroupRecyclerView = findViewById(R.id.group_list_recycler_view)
        val groupNameEditText = findViewById<EditText>(R.id.user_group_name)
        val createGroupButton = findViewById<Button>(R.id.create_group_button)
        val joinGroupButton = findViewById<Button>(R.id.join_group_button)
        setLayoutManagerToRecyclerView()

        userGroupPresenter = UserGroupPresenter(this)
        injectComponents(userGroupPresenter)

        val sharedPreferences = getSharedPreferences(Constant.SIMPLE_CHAT_SHARED_PREF, Context.MODE_PRIVATE)
        userToken = SharedPreferenceModule(sharedPreferences).getUserToken()
        userGroupPresenter.populateUserGroups(userToken)

        createGroupButton.setOnClickListener {
            val groupName = groupNameEditText.text.toString()
            userGroupPresenter.createGroupButtonClicked(userToken, groupName)
        }

        joinGroupButton.setOnClickListener {
            val groupName = groupNameEditText.text.toString()
            userGroupPresenter.joinGroupButtonClicked(userToken, groupName)
        }
    }

    private fun injectComponents(userGroupPresenter: UserGroupPresenter) {
        val daggerUserGroupComponents = DaggerSimpleChatDeps.builder().build()
        daggerUserGroupComponents.inject(userGroupPresenter)
    }

    private fun setLayoutManagerToRecyclerView() {
        userGroupRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
    }

    override fun onGetUserGroupSuccessFetch(userGroupResponse: UserGroupResponse) {
        userGroupList = userGroupResponse.data.userGroupList
        userGroupRecyclerView.adapter = UserGroupAdapter(userGroupList) { groupId: String, groupName: String -> setGroupCardClickListener(groupId, groupName) }
    }

    override fun setGroupCardClickListener(groupId: String, groupName: String) {
        userGroupPresenter.groupCardClicked(groupId, groupName)
    }

    override fun navigateToGroupChatUI(groupId: String, groupName: String) {
        val intent = Intent(this, GroupMessagesActivity::class.java)
        intent.putExtra("groupId", groupId)
        intent.putExtra("groupName", groupName)
        // TODO: Sent the group ID and groupName to Next Activity
    }

    override fun onGetUserGroupFailedFetch() {
        // TODO: Handle Error When Server Error
    }

    override fun showAlert(status: String) {
        Toast.makeText(applicationContext, status, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateGroupSuccess(createGroupResponseBody: CreateGroupResponseBody) {
        val newUserGroup = UserGroup(createGroupResponseBody.data.id, createGroupResponseBody.data.name)
        userGroupList.add(newUserGroup)
        userGroupRecyclerView.adapter?.notifyItemInserted(userGroupList.size - 1)
        userGroupRecyclerView.smoothScrollToPosition(userGroupList.size - 1)
        Toast.makeText(this, getString(R.string.create_group_success_message), Toast.LENGTH_LONG).show()
    }

    override fun onCreateGroupFailed() {
        Toast.makeText(this, getString(R.string.create_group_failure_message), Toast.LENGTH_LONG).show()
    }

    override fun onJoinGroupSuccess(joinGroupResponseBody: JoinGroupResponseBody) {
        val newUserGroup = UserGroup(joinGroupResponseBody.data.id, joinGroupResponseBody.data.name)
        userGroupList.add(newUserGroup)
        userGroupRecyclerView.adapter?.notifyItemInserted(userGroupList.size - 1)
        userGroupRecyclerView.smoothScrollToPosition(userGroupList.size - 1)
        Toast.makeText(this, getString(R.string.join_group_success_message), Toast.LENGTH_LONG).show()
    }

    override fun onJoinGroupFailed(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun groupNameIsEmptyMessage() {
        Toast.makeText(this, getString(R.string.group_name_cannot_be_empty_message), Toast.LENGTH_LONG).show()
    }

    companion object {
        fun navigateToUserGroup(currentActivity: Activity): Intent {
            return Intent(currentActivity, UserGroupActivity::class.java)
        }

    }
}
