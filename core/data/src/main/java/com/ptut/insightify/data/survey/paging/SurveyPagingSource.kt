package com.ptut.insightify.data.survey.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ptut.insightify.data.survey.model.SurveyDto
import com.ptut.insightify.data.survey.service.SurveyApiService
import com.ptut.insightify.data.survey.util.mapToDomain
import com.ptut.insightify.domain.survey.model.Survey

class SurveyPagingSource(
    private val apiService: SurveyApiService,
) : PagingSource<Int, Survey>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Survey> {
        return try {
            val pageNumber = params.key ?: 1
            val pageSize = params.loadSize
            val response = apiService.fetchSurveys(pageNumber, pageSize)

            if (response.data.isEmpty()) {
                LoadResult.Error(Exception("No more data"))
            } else {
                LoadResult.Page(
                    data = response.data.map(SurveyDto::mapToDomain),
                    prevKey = if (pageNumber == 1) null else pageNumber - 1,
                    nextKey = if (response.data.isEmpty()) null else pageNumber + 1,
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Survey>): Int? {
        return state.anchorPosition
    }
}
