package com.ptut.insightify.data.profile.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val attributes: ProfileAttributes,
    val id: String,
    val type: String,
)
