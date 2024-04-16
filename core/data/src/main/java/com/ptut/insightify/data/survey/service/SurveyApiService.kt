package com.ptut.insightify.data.survey.service

import com.ptut.insightify.data.survey.model.SurveyResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SurveyApiService {
    @GET("/api/v1/surveys")
    suspend fun fetchSurveys(
        @Query("page[number]") page: Int,
        @Query("page[size]") size: Int,
    ): SurveyResponseDto
}
