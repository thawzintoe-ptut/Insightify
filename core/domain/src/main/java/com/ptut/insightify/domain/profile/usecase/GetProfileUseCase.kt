package com.ptut.insightify.domain.profile.usecase

import com.ptut.insightify.common.error.DataError
import com.ptut.insightify.common.error.Result
import com.ptut.insightify.domain.profile.model.UserProfile
import com.ptut.insightify.domain.profile.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    operator fun invoke(): Flow<Result<UserProfile, DataError.Network>> {
        return profileRepository.getProfile()
    }
}
