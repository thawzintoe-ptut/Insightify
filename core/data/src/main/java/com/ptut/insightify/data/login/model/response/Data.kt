package com.ptut.insightify.data.login.model.response

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val attributes: Attributes,
    val id: String,
    val type: String
)