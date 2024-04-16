package com.ptut.insightify.data.profile.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponseDto(
    val data: ProfileDto,
)