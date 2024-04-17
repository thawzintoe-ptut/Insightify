package com.ptut.insightify.data.survey.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Meta(
    val page: Int,
    @SerialName("page_size") val pageSize: Int,
    val pages: Int,
    val records: Int,
)
