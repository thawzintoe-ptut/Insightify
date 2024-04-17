package com.ptut.insightify.data.profile.remote

import com.ptut.insightify.data.profile.mapper.mapToDomain
import com.ptut.insightify.data.profile.service.ProfileApiService
import com.ptut.insightify.domain.profile.model.UserProfile
import com.ptut.insightify.domain.profile.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileApiService: ProfileApiService,
) : ProfileRepository {
    override fun getProfile(): Flow<UserProfile> =
        flow {
            val result =
                profileApiService.fetchUserProfile()
            if (result.isSuccessful) {
                result.body()?.let {
                    emit(it.data.attributes.mapToDomain())
                }
            }
        }
}
