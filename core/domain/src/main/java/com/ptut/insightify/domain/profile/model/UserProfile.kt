package com.ptut.insightify.domain.profile.model

data class UserProfile(
    val profileImageUrl: String,
    val email: String,
    val name: String,
    val currentDate: String,
)
