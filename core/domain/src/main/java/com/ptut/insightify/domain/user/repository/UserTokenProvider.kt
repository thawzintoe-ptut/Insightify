package com.ptut.insightify.domain.user.repository

interface UserTokenProvider {
    fun getAccessToken(): String
    fun saveAccessToken(token: String)
    fun getRefreshToken(): String
    fun saveRefreshToke(token: String)
}
