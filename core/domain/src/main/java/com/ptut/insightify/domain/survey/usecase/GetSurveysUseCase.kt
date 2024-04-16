package com.ptut.insightify.domain.survey.usecase

import com.ptut.insightify.common.error.DataError
import com.ptut.insightify.common.error.Result
import com.ptut.insightify.domain.profile.repository.ProfileRepository
import com.ptut.insightify.domain.survey.model.ProfileWithSurveys
import com.ptut.insightify.domain.survey.repository.SurveyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSurveysUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val surveysRepository: SurveyRepository,
) {
    operator fun invoke(): Flow<Result<ProfileWithSurveys, DataError.Network>> = combine(
        profileRepository.getProfile(),
        surveysRepository.fetchSurveyList(),
    ) { profile, surveys ->
        profile to surveys
    }.map { (profile, surveys) ->
        when (profile) {
            is Result.Error -> Result.Error(profile.error)
            is Result.Success -> Result.Success(
                ProfileWithSurveys(
                    profile = profile.data,
                    surveys = surveys,
                ),
            )
        }
    }
}
