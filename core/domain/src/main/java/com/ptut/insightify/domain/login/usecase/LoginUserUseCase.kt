package com.ptut.insightify.domain.login.usecase

import com.ptut.insightify.common.error.DataError
import com.ptut.insightify.common.error.Result
import com.ptut.insightify.domain.login.repository.LoginRepository
import com.ptut.insightify.domain.user.repository.UserTokenProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
    private val userTokenProvider: UserTokenProvider
) {
    operator fun invoke(
        email: String,
        password: String
    ): Flow<Result<Unit, DataError.Network>> {
            return loginRepository.fetchSurveyToken(email, password)
                .map {
                    when (it) {
                        is Result.Error -> {
                            Result.Error(it.error)
                        }
					
                        is Result.Success -> {
                            userTokenProvider.saveAccessToken(it.data.token)
                            userTokenProvider.saveRefreshToke(it.data.refreshToken?:"")
                            Result.Success(Unit)
                        }
                    }
                }
        }
    }
