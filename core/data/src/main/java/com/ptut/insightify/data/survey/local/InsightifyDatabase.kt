package com.ptut.insightify.data.survey.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [SurveyEntity::class, RemoteKeys::class],
    version = 1,
    exportSchema = false,
)
abstract class InsightifyDatabase : RoomDatabase() {
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun surveyDao(): SurveyDao
}