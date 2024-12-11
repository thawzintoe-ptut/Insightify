package com.ptut.insightify.domain.login.model

data class AuthToken(
    val userId: String,
    val token: String,
    val refreshToken: String?,
)
