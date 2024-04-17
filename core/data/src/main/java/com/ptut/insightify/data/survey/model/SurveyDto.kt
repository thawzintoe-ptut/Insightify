package com.ptut.insightify.data.survey.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SurveyDto(
    @SerialName("attributes") val survey: SurveyAttributes,
    val id: String,
    val type: String,
)
