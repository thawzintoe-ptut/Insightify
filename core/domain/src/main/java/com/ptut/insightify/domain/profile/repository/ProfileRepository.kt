package com.ptut.insightify.domain.profile.repository

import com.ptut.insightify.common.error.DataError
import com.ptut.insightify.common.error.Result
import com.ptut.insightify.domain.profile.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<Result<UserProfile, DataError.Network>>
}