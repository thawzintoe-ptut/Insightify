package com.ptut.insightify.data.survey.util

import com.ptut.insightify.data.survey.local.SurveyEntity
import com.ptut.insightify.data.survey.model.SurveyDto
import com.ptut.insightify.domain.survey.model.Survey

fun SurveyDto.mapToDomain() =
    Survey(
        id = id,
        type = type,
        coverImageUrl = this.survey.coverImageUrl,
        description = this.survey.description,
        isActive = this.survey.isActive,
        surveyType = this.survey.surveyType,
        title = this.survey.title,
    )

fun SurveyDto.mapToEntity() =
    SurveyEntity(
        id = id,
        type = type,
        coverImageUrl = this.survey.coverImageUrl,
        description = this.survey.description,
        isActive = this.survey.isActive,
        surveyType = this.survey.surveyType,
        title = this.survey.title,
    )
