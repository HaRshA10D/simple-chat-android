package com.gojek.simplechat.userGroup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.*
import com.gojek.simplechat.R
import com.gojek.simplechat.constants.Constant
import com.gojek.simplechat.datastore.SharedPreferenceModule
import com.gojek.simplechat.deps.DaggerSimpleChatDeps
import com.gojek.simplechat.groupMessage.GroupMessagesActivity
import com.gojek.simplechat.userGroup.adapter.UserGroupAdapter
import com.gojek.simplechat.userGroup.model.CreateGroupResponseBody
import com.gojek.simplechat.userGroup.model.JoinGroupResponseBody
import com.gojek.simplechat.userGroup.model.UserGroup
import com.gojek.simplechat.userGroup.model.UserGroupResponse

class UserGroupActivity : AppCompatActivity(), UserGroupView {

    private lateinit var userGroupRecyclerView: RecyclerView
    private lateinit var userGroupLoadingBar: ProgressBar
    private lateinit var userGroupNetworkErrorTextView: TextView
    private lateinit var groupNameEditText: EditText
    private lateinit var userGroupPresenter: UserGroupPresenter
    private lateinit var userToken: String
    private lateinit var userGroupList: MutableList<UserGroup>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_group)

        userGroupRecyclerView = findViewById(R.id.group_list_recycler_view)
        userGroupLoadingBar = findViewById(R.id.user_group_loading_bar)
        userGroupNetworkErrorTextView = findViewById(R.id.user_group_network_error)
        groupNameEditText = findViewById(R.id.user_group_name)
        val createGroupButton = findViewById<Button>(R.id.create_group_button)
        val joinGroupButton = findViewById<Button>(R.id.join_group_button)
        setLayoutManagerToRecyclerView()
        showLoadingScreen()

        userGroupPresenter = UserGroupPresenter(this)
        injectComponents(userGroupPresenter)

        val sharedPreferences = getSharedPreferences(Constant.SIMPLE_CHAT_SHARED_PREF, Context.MODE_PRIVATE)
        userToken = SharedPreferenceModule(sharedPreferences).getUserToken()
        userGroupPresenter.populateUserGroups(userToken)

        createGroupButton.setOnClickListener {
            val groupName = groupNameEditText.text.toString()
            userGroupPresenter.isValidUserGroupName(groupName)
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

    private fun showLoadingScreen() {
        userGroupNetworkErrorTextView.visibility = TextView.GONE
        userGroupRecyclerView.visibility = RecyclerView.GONE
        userGroupLoadingBar.visibility = ProgressBar.VISIBLE
    }

    private fun showListScreen() {
        userGroupNetworkErrorTextView.visibility = TextView.GONE
        userGroupRecyclerView.visibility = RecyclerView.VISIBLE
        userGroupLoadingBar.visibility = ProgressBar.GONE
    }

    private fun showErrorScreen() {
        userGroupNetworkErrorTextView.visibility = TextView.VISIBLE
        userGroupRecyclerView.visibility = RecyclerView.GONE
        userGroupLoadingBar.visibility = ProgressBar.GONE
    }

    override fun onGetUserGroupSuccessFetch(userGroupResponse: UserGroupResponse) {
        userGroupList = userGroupResponse.data.userGroupList
        userGroupRecyclerView.adapter = UserGroupAdapter(userGroupList) { groupId: String, groupName: String -> setGroupCardClickListener(groupId, groupName) }
        showListScreen()
    }

    override fun setGroupCardClickListener(groupId: String, groupName: String) {
        userGroupPresenter.groupCardClicked(groupId, groupName)
    }

    override fun navigateToGroupChatUI(groupId: String, groupName: String) {
        val intent = GroupMessagesActivity.navigateToGroupMessageActivity(this)
        intent.putExtra(Constant.GROUP_ID, groupId)
        intent.putExtra(Constant.GROUP_NAME, groupName)
        startActivity(intent)
    }

    override fun onGetUserGroupFailedFetch() {
        showErrorScreen()
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

    override fun groupNameIsNotAlphanumeric() {
        groupNameEditText.error = getString(R.string.invalid_group_name_error_message)
    }

    override fun createUserGroup(userGroupName: String) {
        userGroupPresenter.createGroupButtonClicked(userToken, userGroupName)
    }

    companion object {
        fun navigateToUserGroup(currentActivity: Activity): Intent {
            return Intent(currentActivity, UserGroupActivity::class.java)
        }

    }
}
