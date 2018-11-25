package com.gojek.simplechat.datastore

import android.content.SharedPreferences
import com.gojek.simplechat.constants.Constant

class SharedPreferenceModule(private val sharedPreferences: SharedPreferences) {

    fun setUserToken(token: String) {
        val storeToken = sharedPreferences.edit()
        storeToken.putString(Constant.USER_TOKEN, token)
        storeToken.apply()
    }

    fun getUserToken(): String? {
        return sharedPreferences.getString(Constant.USER_TOKEN, Constant.USER_TOKEN_DEFAULT)
    }
}
