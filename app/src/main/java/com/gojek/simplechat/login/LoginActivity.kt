package com.gojek.simplechat.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.gojek.simplechat.R
import com.gojek.simplechat.deps.DaggerSimpleChatDeps
import com.gojek.simplechat.deps.SimpleChatDeps

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
                loginPresenter.loginUser(userName)
            } else {
                Toast.makeText(this, getString(R.string.invalid_name_error_msg), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun goToHome(token: String, responseMessage: String) {
        Toast.makeText(this, responseMessage, Toast.LENGTH_LONG).show()
        // TODO: Add intent to go to group list screen from here
    }

    override fun loginFailed() {
        Toast.makeText(this, getString(R.string.unexpected_error_msg), Toast.LENGTH_LONG).show()
    }
}
