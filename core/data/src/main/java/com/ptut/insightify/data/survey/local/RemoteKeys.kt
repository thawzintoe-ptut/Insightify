package com.ptut.insightify.data.survey.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val repoId: String,
    val prevKey: Int?,
    val nextKey: Int?,
)
