package com.ptut.insightify.data.profile.remote

import com.ptut.insightify.common.error.DataError
import com.ptut.insightify.common.error.Result
import com.ptut.insightify.common.util.handleError
import com.ptut.insightify.data.profile.mapper.mapToDomain
import com.ptut.insightify.data.profile.service.ProfileApiService
import com.ptut.insightify.domain.profile.model.UserProfile
import com.ptut.insightify.domain.profile.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileApiService: ProfileApiService,
) : ProfileRepository {
    override fun getProfile(): Flow<Result<UserProfile, DataError.Network>> =
        flow<Result<UserProfile, DataError.Network>> {
            val result =
                profileApiService.fetchUserProfile()
            if (result.isSuccessful) {
                result.body()?.let {
                    emit(Result.Success(it.data.attributes.mapToDomain()))
                }
            } else {
                emit(Result.Error(DataError.Network.UNKNOWN))
            }
        }.catch { exception ->
            handleError(exception = exception, send = { error -> emit(error) })
        }
}
