package com.ptut.insightify.data.survey.remote

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.ptut.insightify.data.survey.local.SurveyEntity
import com.ptut.insightify.data.survey.util.mapToDomain
import com.ptut.insightify.domain.survey.model.Survey
import com.ptut.insightify.domain.survey.repository.SurveyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SurveyRepositoryImpl @Inject constructor(
    private val surveyPager: Pager<Int, SurveyEntity>,
) : SurveyRepository {

    override fun fetchSurveyList(): Flow<PagingData<Survey>> {
        return surveyPager.flow.map { pagingData ->
            pagingData.map(SurveyEntity::mapToDomain)
        }
    }

    override fun refreshSurveys() {

    }

    companion object {
        const val NETWORK_PAGE_SIZE = 5
    }
}