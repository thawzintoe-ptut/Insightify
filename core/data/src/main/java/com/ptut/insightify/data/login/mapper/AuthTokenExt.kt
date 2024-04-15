package com.ptut.insightify.data.login.mapper

import com.ptut.insightify.data.login.model.response.LoginResponseDto
import com.ptut.insightify.domain.login.model.AuthToken

fun LoginResponseDto.mapToDomain(): AuthToken {
    return AuthToken(
        userId = this.data.id,
        token = this.data.attributes.accessToken,
        refreshToken = this.data.attributes.refreshToken,
    )
}
