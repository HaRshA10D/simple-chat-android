package com.gojek.simplechat.login

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.gojek.simplechat.R
import com.gojek.simplechat.constants.Constant
import com.gojek.simplechat.datastore.SharedPreferenceModule
import com.gojek.simplechat.deps.DaggerSimpleChatDeps
import com.gojek.simplechat.deps.SimpleChatDeps
import com.gojek.simplechat.userGroup.UserGroupActivity

class LoginActivity : AppCompatActivity(), LoginView {

    private lateinit var simpleChatDeps: SimpleChatDeps

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val userNameEditText = findViewById<EditText>(R.id.input_name)
        val submitNameButton = findViewById<Button>(R.id.submit_name)
        simpleChatDeps = DaggerSimpleChatDeps.builder().build()
        val loginPresenter = LoginPresenter(this)
        simpleChatDeps.inject(loginPresenter)

        submitNameButton.setOnClickListener {
            val userName = userNameEditText?.text.toString()
            if (loginPresenter.isNameValid(userName)) {
                loginPresenter.loginUser(userName.toLowerCase())
            } else {
                Toast.makeText(this, getString(R.string.invalid_name_error_msg), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun goToHome(token: String, responseMessage: String) {
        saveTokenToSharedPreference(token)
        startActivity(UserGroupActivity.navigateToUserGroup(this))
    }

    private fun saveTokenToSharedPreference(token: String){
        val sharedPreference = getSharedPreferences(Constant.SIMPLE_CHAT_SHARED_PREF, Context.MODE_PRIVATE)
        val saveToken = SharedPreferenceModule(sharedPreference)
        saveToken.setUserToken(token)
    }

    override fun loginFailed() {
        Toast.makeText(this, getString(R.string.unexpected_error_msg), Toast.LENGTH_LONG).show()
    }
}
