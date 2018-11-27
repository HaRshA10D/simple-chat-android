package com.gojek.simplechat.groupMessage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.gojek.simplechat.R
import com.gojek.simplechat.constants.Constant
import com.gojek.simplechat.datastore.SharedPreferenceModule
import com.gojek.simplechat.deps.DaggerSimpleChatDeps
import com.gojek.simplechat.groupMessage.adapter.GroupMessagesAdapter
import com.gojek.simplechat.groupMessage.model.GroupMessagesResponse

class GroupMessagesActivity : AppCompatActivity(), GroupMessagesView {

    private lateinit var groupMessagesListView: RecyclerView
    private lateinit var groupMessagesLoadingView: View
    private lateinit var groupMessagesErrorView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_messages)
        val groupMessagePresenter = GroupMessagesPresenter(this)
        injectComponents(groupMessagePresenter)
        bindTheView()
        val refreshButton = findViewById<Button>(R.id.group_messages_refresh)
        val sendMessageButton = findViewById<Button>(R.id.group_messages_send)
        val textMessageEditText = findViewById<EditText>(R.id.group_messages_edittext_message)
        setLayoutManager()
        setActionBar()
        showLoadingScreen()
        val sharedPreferences = getSharedPreferences(Constant.SIMPLE_CHAT_SHARED_PREF, Context.MODE_PRIVATE)
        val userToken = SharedPreferenceModule(sharedPreferences).getUserToken()
        groupMessagePresenter.groupMessages(groupId(), userToken)
        refreshButton.setOnClickListener {
            groupMessagePresenter.groupMessages(groupId(), userToken)
        }

        sendMessageButton.setOnClickListener {
            val sendMessageTime = System.currentTimeMillis().toString()
            val textMessage = textMessageEditText.text.toString()
            groupMessagePresenter.sendMessageButtonClicked(userToken, groupId(), textMessage, sendMessageTime)
            Handler().postDelayed({
                groupMessagePresenter.groupMessages(groupId(), userToken)
            }, Constant.HUNDRED_MILLISECOND)
        }
    }

    override fun onResume() {
        super.onResume()
        setActionBar()
    }

    private fun setActionBar() {
        val groupName = intent.getStringExtra(Constant.GROUP_NAME)
        supportActionBar?.title = groupName
    }


    private fun groupId(): String {
        val groupId = intent.getStringExtra(Constant.GROUP_ID)
        return groupId
    }

    private fun injectComponents(groupMessagePresenter: GroupMessagesPresenter) {
        val daggerMessagesComponent = DaggerSimpleChatDeps.builder().build()
        daggerMessagesComponent.inject(groupMessagePresenter)
    }

    private fun setLayoutManager() {
        groupMessagesListView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
    }

    private fun bindTheView() {
        groupMessagesListView = findViewById(R.id.group_messages_recycler_view)
        groupMessagesLoadingView = findViewById(R.id.group_messages_loading_bar)
        groupMessagesErrorView = findViewById(R.id.group_messages_network_error)
    }

    override fun populateGroupMessages(groupMessagesResponse: GroupMessagesResponse) {
        showListScreen()
        val groupMessagesAdapter = GroupMessagesAdapter(groupMessagesResponse.data.groupMessageList)
        groupMessagesListView.adapter = groupMessagesAdapter
    }

    private fun showListScreen() {
        groupMessagesErrorView.visibility = View.GONE
        groupMessagesLoadingView.visibility = View.GONE
        groupMessagesListView.visibility = View.VISIBLE
    }

    private fun showLoadingScreen() {
        groupMessagesErrorView.visibility = View.GONE
        groupMessagesListView.visibility = View.GONE
        groupMessagesLoadingView.visibility = View.VISIBLE
    }

    override fun showNetworkError() {
        groupMessagesErrorView.visibility = View.VISIBLE
        groupMessagesListView.visibility = View.GONE
        groupMessagesLoadingView.visibility = View.GONE
    }

    override fun showCustomError(errorMessage: String?) {
        showNetworkError()
        groupMessagesErrorView.text = errorMessage
    }

    override fun onSendMessageSuccessful() {
        Toast.makeText(this, getString(R.string.send_message_successful), Toast.LENGTH_LONG).show()
    }

    override fun onSendMessageFailed() {
        Toast.makeText(this, getString(R.string.message_send_failure), Toast.LENGTH_LONG).show()
    }

    companion object {

        fun navigateToGroupMessageActivity(currentActivity: Activity): Intent {
            return Intent(currentActivity, GroupMessagesActivity::class.java)
        }
    }
}
