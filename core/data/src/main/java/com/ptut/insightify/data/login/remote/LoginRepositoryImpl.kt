package com.ptut.insightify.data.login.remote

import com.ptut.insightify.common.error.DataError
import com.ptut.insightify.common.error.Result
import com.ptut.insightify.common.util.handleError
import com.ptut.insightify.data.BuildConfig
import com.ptut.insightify.data.login.mapper.mapToDomain
import com.ptut.insightify.data.login.model.request.LoginRequestDto
import com.ptut.insightify.data.login.service.LoginApiService
import com.ptut.insightify.domain.login.model.AuthToken
import com.ptut.insightify.domain.login.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginApiService: LoginApiService,
) : LoginRepository {
    override fun fetchSurveyToken(
        email: String,
        password: String,
    ): Flow<Result<AuthToken, DataError.Network>> =
        flow {
            val result =
                loginApiService.fetchSurveyToken(
                    LoginRequestDto(
                        grantType = BuildConfig.GRANT_TYPE,
                        email = email,
                        password = password,
                        clientId = BuildConfig.CLIENT_ID,
                        clientSecret = BuildConfig.CLIENT_SECRET,
                    ),
                )
            if (result.isSuccessful) {
                result.body()?.let {
                    emit(Result.Success(it.mapToDomain()))
                } ?: run {
                    emit(Result.Error(DataError.Network.UNKNOWN))
                }
            }
        }.catch { exception ->
            handleError(exception = exception, send = { error -> emit(error) })
        }
}
