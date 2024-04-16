package com.ptut.insightify.data.survey.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ptut.insightify.data.survey.paging.SurveyPagingSource
import com.ptut.insightify.data.survey.service.SurveyApiService
import com.ptut.insightify.domain.survey.model.Survey
import com.ptut.insightify.domain.survey.repository.SurveyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SurveyRepositoryImpl
@Inject
constructor(
    private val apiService: SurveyApiService,
) : SurveyRepository {
    private var currentPagingSource: SurveyPagingSource? = null

    override fun fetchSurveyList(): Flow<PagingData<Survey>> {
        return Pager(
            config = PagingConfig(pageSize = 5, enablePlaceholders = true),
            pagingSourceFactory = {
                SurveyPagingSource(apiService).also {
                    currentPagingSource = it
                }
            },
        ).flow
    }

    override fun refreshSurveys() {
        currentPagingSource?.invalidate()
    }
}
