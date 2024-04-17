package com.ptut.insightify.domain.survey.usecase

import com.ptut.insightify.domain.profile.repository.ProfileRepository
import com.ptut.insightify.domain.survey.model.ProfileWithSurveys
import com.ptut.insightify.domain.survey.repository.SurveyRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSurveysUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val surveysRepository: SurveyRepository,
) {
    @OptIn(FlowPreview::class)
    operator fun invoke(): Flow<ProfileWithSurveys> = profileRepository.getProfile()
        .flatMapConcat { profile ->
            surveysRepository.fetchSurveyList()
                .map { surveys ->
                    ProfileWithSurveys(profile, surveys)
                }
        }
}
