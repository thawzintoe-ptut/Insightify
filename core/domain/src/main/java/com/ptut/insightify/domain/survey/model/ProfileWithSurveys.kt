package com.ptut.insightify.domain.survey.model

import androidx.paging.PagingData
import com.ptut.insightify.domain.profile.model.UserProfile

data class ProfileWithSurveys(
    val profile: UserProfile,
    val surveys: PagingData<Survey>,
)
