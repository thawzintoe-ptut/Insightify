package com.ptut.insightify.data.survey.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SurveyAttributes(
    @SerialName("active_at") val activeAt: String,
    @SerialName("cover_image_url") val coverImageUrl: String,
    @SerialName("created_at") val createdAt: String,
    val description: String,
    @SerialName("is_active") val isActive: Boolean,
    @SerialName("survey_type") val surveyType: String,
    val title: String,
)
