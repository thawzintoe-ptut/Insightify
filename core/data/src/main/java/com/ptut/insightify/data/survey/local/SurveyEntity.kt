package com.ptut.insightify.data.survey.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "survey")
data class SurveyEntity(
    @PrimaryKey
    val id: String,
    val type: String,
    val coverImageUrl: String,
    val description: String,
    val isActive: Boolean,
    val surveyType: String,
    val title: String,
)