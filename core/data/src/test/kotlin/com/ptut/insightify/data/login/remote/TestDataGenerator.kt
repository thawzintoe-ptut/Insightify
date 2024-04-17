package com.ptut.insightify.data.login.remote

import com.ptut.insightify.data.login.model.response.Attributes
import com.ptut.insightify.data.login.model.response.Data
import com.ptut.insightify.data.login.model.response.LoginResponseDto

fun loginResponseDto() = LoginResponseDto(
    data = Data(
        attributes = Attributes(
            accessToken = "token",
            tokenType = "Bearer",
            expiresIn = 3600,
            refreshToken = "refresh_token",
            createdAt = 1631539200,
        ),
        id = "1",
        type = "auth_tokens",
    ),
)