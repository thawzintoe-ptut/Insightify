package com.ptut.insightify.data.login.local.user

import android.content.SharedPreferences
import com.ptut.insightify.domain.user.repository.UserTokenProvider
import javax.inject.Inject

class UserTokenSharedPreferencesProvider @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : UserTokenProvider {

    override fun getAccessToken(): String {
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, "") ?: ""
    }

    override fun saveAccessToken(token: String) {
        sharedPreferences.edit().putString(ACCESS_TOKEN_KEY, token).apply()
    }

    override fun getRefreshToken(): String {
        return sharedPreferences.getString(REFRESH_TOKEN_KEY, "") ?: ""
    }

    override fun saveRefreshToke(token: String) {
        sharedPreferences.edit().putString(REFRESH_TOKEN_KEY, token).apply()
    }

    companion object {
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
    }
}
