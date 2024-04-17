package com.ptut.insightify.data.survey.util

import com.ptut.insightify.data.survey.local.SurveyEntity
import com.ptut.insightify.domain.survey.model.Survey

fun SurveyEntity.mapToDomain() = Survey(
    id = id,
    type = type,
    coverImageUrl = coverImageUrl,
    description = description,
    isActive = isActive,
    surveyType = surveyType,
    title = title,
)