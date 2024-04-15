package com.ptut.insightify.data.login.service

import com.ptut.insightify.data.login.model.request.LoginRequestDto
import com.ptut.insightify.data.login.model.response.LoginResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiService {
    @POST("/api/v1/oauth/token")
    suspend fun fetchSurveyToken(
        @Body loginRequestDto: LoginRequestDto
    ): Response<LoginResponseDto>
}