package com.ptut.insightify.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ptut.insightify.domain.survey.model.Survey

class TestPagingSource : PagingSource<Int, Survey>() {
    override fun getRefreshKey(state: PagingState<Int, Survey>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Survey> {
        return LoadResult.Page(
            data = dummySurveys,
            prevKey = null,
            nextKey = 3,
        )
    }
}