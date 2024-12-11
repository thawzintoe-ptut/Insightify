package com.ptut.insightify.domain.profile.repository

import com.ptut.insightify.domain.profile.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<UserProfile>
}
