package com.ptut.insightify.domain.profile.usecase

import com.ptut.insightify.domain.profile.model.UserProfile
import com.ptut.insightify.domain.profile.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    operator fun invoke(): Flow<UserProfile> {
        return profileRepository.getProfile()
    }
}
