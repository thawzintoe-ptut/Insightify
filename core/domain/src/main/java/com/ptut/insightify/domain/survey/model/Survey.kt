package com.ptut.insightify.domain.survey.model

data class Survey(
    val id: String,
    val type: String,
    val coverImageUrl: String,
    val description: String,
    val isActive: Boolean,
    val surveyType: String,
    val title: String,
)
