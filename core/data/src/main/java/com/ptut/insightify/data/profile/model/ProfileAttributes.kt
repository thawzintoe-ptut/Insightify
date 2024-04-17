package com.ptut.insightify.data.profile.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileAttributes(
    @SerialName("avatar_url")
    val avatarUrl: String,
    val email: String,
    val name: String,
)