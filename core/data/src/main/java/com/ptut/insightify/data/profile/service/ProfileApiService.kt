package com.ptut.insightify.data.profile.service

import com.ptut.insightify.data.profile.model.ProfileResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface ProfileApiService {
    @GET("/api/v1/me")
    suspend fun fetchUserProfile(): Response<ProfileResponseDto>
}