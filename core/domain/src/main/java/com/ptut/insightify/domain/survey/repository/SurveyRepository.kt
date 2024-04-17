package com.ptut.insightify.domain.survey.repository

import androidx.paging.PagingData
import com.ptut.insightify.domain.survey.model.Survey
import kotlinx.coroutines.flow.Flow

interface SurveyRepository {
    fun fetchSurveyList(): Flow<PagingData<Survey>>

    fun refreshSurveys()
}
