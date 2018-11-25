package com.gojek.simplechat.userGroup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.Toast
import com.gojek.simplechat.R
import com.gojek.simplechat.constants.Constant
import com.gojek.simplechat.datastore.SharedPreferenceModule
import com.gojek.simplechat.deps.DaggerSimpleChatDeps
import com.gojek.simplechat.groupMessage.GroupMessagesActivity
import com.gojek.simplechat.userGroup.adapter.UserGroupAdapter
import com.gojek.simplechat.userGroup.model.UserGroupResponse

class UserGroupActivity : AppCompatActivity(), UserGroupView {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userGroupPresenter: UserGroupPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_group)
        userGroupPresenter = UserGroupPresenter(this)
        injectComponents(userGroupPresenter)
        val sharedPreferences = getSharedPreferences(Constant.SIMPLE_CHAT_SHARED_PREF, Context.MODE_PRIVATE)
        val userToken = SharedPreferenceModule(sharedPreferences).getUserToken()
        userGroupPresenter.populateUserGroups(userToken!!)
        bindView()
        setLayoutManagerToRecyclerView()
    }

    private fun injectComponents(userGroupPresenter: UserGroupPresenter) {
        val daggerUserGroupComponents = DaggerSimpleChatDeps.builder().build()
        daggerUserGroupComponents.inject(userGroupPresenter)
    }

    private fun bindView() {
        recyclerView = findViewById(R.id.group_list_recycler_view)
    }

    private fun setLayoutManagerToRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
    }

    override fun onGetUserGroupSuccessFetch(userGroupResponse: UserGroupResponse) {
        recyclerView.adapter = UserGroupAdapter(userGroupResponse.data.userGroupList) { groupId: String, groupName: String -> setGroupCardClickListener(groupId, groupName) }
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

    companion object {
        fun navigateToUserGroup(currentActivity: Activity): Intent {
            return Intent(currentActivity, UserGroupActivity::class.java)
        }

    }
}
