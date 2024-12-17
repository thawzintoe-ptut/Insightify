package com.ptut.insightify.domain.survey.usecase

import com.ptut.insightify.domain.survey.repository.SurveyRepository
import javax.inject.Inject

class RefreshSurveysUseCase
@Inject
constructor(
    private val surveysRepository: SurveyRepository,
) {
    operator fun invoke() = surveysRepository.refreshSurveys()
}
