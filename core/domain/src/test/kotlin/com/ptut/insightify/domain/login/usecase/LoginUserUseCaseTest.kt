package com.ptut.insightify.domain.login.usecase

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.ptut.insightify.common.error.DataError
import com.ptut.insightify.common.error.Result
import com.ptut.insightify.domain.login.model.AuthToken
import com.ptut.insightify.domain.login.repository.LoginRepository
import com.ptut.insightify.domain.user.repository.UserTokenProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class LoginUserUseCaseTest {
    @get: Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var loginUserUseCase: LoginUserUseCase
    private val loginRepository: LoginRepository = mock()
    private val userTokenProvider: UserTokenProvider = mock()

    @Before
    fun setUp() {
        loginUserUseCase = LoginUserUseCase(loginRepository, userTokenProvider)
    }

    @Test
    fun `invoke returns success when repository returns success`() = coroutineRule.runTest {
        val email = "test@example.com"
        val password = "password"
        val token = "token123"
        val refreshToken = "refreshToken123"
        whenever(loginRepository.fetchSurveyToken(email, password)).thenReturn(
            flowOf(
                Result.Success(
                    AuthToken(
                        "1",
                        token,
                        refreshToken,
                    ),
                ),
            ),
        )

        loginUserUseCase(email, password).test {
            assertThat(awaitItem()).isEqualTo(Result.Success(Unit))
            awaitComplete()
        }

        verify(userTokenProvider).saveAccessToken(token)
        verify(userTokenProvider).saveRefreshToke(refreshToken)
    }

    @Test
    fun `invoke returns error when repository returns error`() =
        coroutineRule.runTest {
            val email = "test@example.com"
            val password = "wrongpassword"
            val error = DataError.Network.UNKNOWN
            whenever(loginRepository.fetchSurveyToken(email, password)).thenReturn(
                flowOf(Result.Error(error)),
            )

            loginUserUseCase(email, password).test {
                val result = awaitItem()
                assertThat(result is Result.Error).isTrue()
                assertThat((result as Result.Error).error).isEqualTo(error)
                awaitComplete()
            }
        }
}
