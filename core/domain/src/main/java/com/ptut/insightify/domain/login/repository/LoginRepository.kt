package com.ptut.insightify.domain.login.repository

import com.ptut.insightify.common.error.DataError
import com.ptut.insightify.common.error.Result
import com.ptut.insightify.domain.login.model.AuthToken
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    fun fetchSurveyToken(
        email: String,
        password: String,
    ): Flow<Result<AuthToken, DataError.Network>>
}
