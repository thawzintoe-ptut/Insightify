package com.ptut.insightify.data.login.remote

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.ptut.insightify.common.error.DataError
import com.ptut.insightify.common.error.Result
import com.ptut.insightify.common.util.ApiErrorType
import com.ptut.insightify.common.util.ApiException
import com.ptut.insightify.data.MainCoroutineRule
import com.ptut.insightify.data.login.service.LoginApiService
import com.ptut.insightify.data.loginResponseDto
import com.ptut.insightify.domain.login.repository.LoginRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.whenever
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class LoginRepositoryImplTest {
    @get: Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var loginRepository: LoginRepository
    private val loginApiService: LoginApiService = mock(LoginApiService::class.java)

    @Before
    fun setup() {
        loginRepository = LoginRepositoryImpl(loginApiService)
    }

    @Test
    fun `fetchSurveyToken returns success when API call is successful`() = coroutineRule.runTest {
        // Given
        val email = "test@gmail.com"
        val password = "password"
        val loginDto = loginResponseDto()
        val response = Response.success(loginDto)
        val expected = loginDto.data.attributes.accessToken

        whenever(loginApiService.fetchSurveyToken(any())).thenReturn(response)

        // When
        loginRepository.fetchSurveyToken(
            email, password,
        ).test {
            val result = awaitItem()
            awaitComplete()
            assertThat(result is Result.Success).isTrue()
            assertThat(expected).isEqualTo((result as Result.Success).data.token)
        }
    }

    @Test
    fun `fetchSurveyToken returns error when API call fails`() = coroutineRule.runTest {
        // Given
        val email = "test@example.com"
        val password = "wrongpassword"
        val expected = Result.Error(DataError.Network.BAD_REQUEST)

        whenever(loginApiService.fetchSurveyToken(any()))
            .doAnswer { throw ApiException(errorType = ApiErrorType.BadRequest) }

        // When
        loginRepository.fetchSurveyToken(email, password).test {
            val result = awaitItem()
            awaitComplete()
            assertEquals(expected, result)
        }
    }
}
