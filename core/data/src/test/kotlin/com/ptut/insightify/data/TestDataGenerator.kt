package com.ptut.insightify.data

import com.ptut.insightify.data.login.model.response.Attributes
import com.ptut.insightify.data.login.model.response.Data
import com.ptut.insightify.data.login.model.response.LoginResponseDto
import com.ptut.insightify.domain.survey.model.Survey

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

val dummySurveys = listOf(
    survey(),
    survey(),
    survey(),
)

fun survey() = Survey(
    id = "1",
    title = "Survey 1",
    description = "Description 1",
    type = "",
    coverImageUrl = "",
    isActive = true,
    surveyType = "",
)
