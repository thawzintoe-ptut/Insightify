package com.ptut.insightify.data.survey.model

import kotlinx.serialization.Serializable

@Serializable
data class SurveyResponseDto(
    val data: List<SurveyDto>,
    val meta: Meta,
)
