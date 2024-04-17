package com.ptut.insightify.data.survey.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SurveyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(surveys: List<SurveyEntity>)

    @Query("SELECT * FROM survey")
    fun pagingSource(): PagingSource<Int, SurveyEntity>

    @Query("DELETE FROM survey")
    suspend fun clearAll()
}