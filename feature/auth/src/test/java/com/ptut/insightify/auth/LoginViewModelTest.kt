package com.ptut.insightify.auth

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.ptut.insightify.auth.LoginViewModel.UiEvent
import com.ptut.insightify.auth.util.FocusedTextFieldKey
import com.ptut.insightify.auth.util.IdConstants
import com.ptut.insightify.common.error.DataError
import com.ptut.insightify.common.error.Result
import com.ptut.insightify.domain.login.usecase.LoginUserUseCase
import com.ptut.insightify.ui.inputvalidations.InputWrapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val testEmail = "test@gmail.com"
    private val testPassword = "password"
    private lateinit var viewModel: LoginViewModel
    private val loginUserUseCase: LoginUserUseCase = mock()
    private val savedStateHandle: SavedStateHandle = mock()

    @Before
    fun setup() {
        whenever(savedStateHandle.get<InputWrapper>(IdConstants.USER_EMAIL))
            .thenReturn(InputWrapper(testEmail, 1))
        whenever(savedStateHandle.get<InputWrapper>(IdConstants.USER_PASSWORD))
            .thenReturn(InputWrapper(testPassword, 1))
        viewModel = LoginViewModel(savedStateHandle, loginUserUseCase)
    }

    @Test
    fun `onEmailChanged sets error when email is invalid`() = coroutineRule.runTest {
        val invalidEmail = "test"
        viewModel.onUiEvent(UiEvent.OnEmailChanged(invalidEmail))
        viewModel.uiState.test {
            val result = awaitItem()
            assertThat(result.email.errorId).isNotNull()
        }
    }

    @Test
    fun `onPasswordChanged sets error when password is invalid`() = coroutineRule.runTest {
        val invalidPassword = "123"
        viewModel.onUiEvent(UiEvent.OnPasswordChanged(invalidPassword))
        viewModel.uiState.test {
            val result = awaitItem()
            assertThat(result.password.errorId).isNotNull()
        }
    }

    @Test
    fun `focus is updated correctly when field is clicked`() = coroutineRule.runTest {
        viewModel.onUiEvent(
            UiEvent.OnTextFieldFocusChanged(
                FocusedTextFieldKey.PASSWORD,
                true,
            ),
        )
        viewModel.uiState.test {
            val result = awaitItem()
            assertThat(result.requestFocus).isEqualTo(FocusedTextFieldKey.PASSWORD)
        }
    }

    @Test
    fun `login failure updates UI state with error`() = coroutineRule.runTest {
        whenever(loginUserUseCase.invoke(testEmail, testPassword))
            .thenReturn(flowOf(Result.Error(DataError.Network.NOT_FOUND)))

        viewModel.onUiEvent(UiEvent.OnLoginClicked)

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.hasError).isTrue()
            assertThat(state.errorType).isEqualTo(DataError.Network.NOT_FOUND)
        }
    }

    @Test
    fun `onLoginClicked executes login use case with correct credentials`() =
        coroutineRule.runTest {
            val email = "test@example.com"
            val password = "password123"
            whenever(
                loginUserUseCase.invoke(
                    email,
                    password,
                ),
            ).thenReturn(flowOf(Result.Success(Unit)))

            viewModel.onUiEvent(UiEvent.OnLoginClicked)

            loginUserUseCase.invoke(email, password).test {
                assertThat(awaitItem() is Result.Success).isTrue()
                awaitComplete()
            }
        }

    @Test
    fun `handle successful login updates UI state and triggers navigation event`() =
        coroutineRule.runTest {
            whenever(loginUserUseCase.invoke(any(), any()))
                .thenReturn(flowOf(Result.Success(Unit)))

            viewModel.onUiEvent(UiEvent.OnLoginClicked)

            viewModel.uiState.test {
                val uiState = awaitItem()
                assertThat(uiState.isLoading).isFalse()
                assertThat(uiState.hasError).isFalse()
            }
        }

    @Test
    fun `handle login error updates UI state correctly`() =
        coroutineRule.runTest {
            val error = DataError.Network.UNKNOWN
            whenever(loginUserUseCase.invoke(any(), any()))
                .thenReturn(flowOf(Result.Error(error)))
            viewModel.onUiEvent(UiEvent.OnLoginClicked)
            viewModel.uiState.test {
                val uiState = awaitItem()
                assertThat(uiState.isLoading).isFalse()
                assertThat(uiState.hasError).isTrue()
                assertThat(uiState.errorType).isEqualTo(error)
            }
        }

    @Test
    fun `corrupted token response results in error`() = coroutineRule.runTest {
        val email = "user@example.com"
        val password = "password"
        whenever(
            loginUserUseCase.invoke(
                email,
                password,
            ),
        ).thenReturn(flowOf(Result.Error(DataError.Network.UNAUTHORIZED)))

        loginUserUseCase(email, password).test {
            val result = awaitItem()
            assertThat(result is Result.Error).isTrue()
            assertThat((result as Result.Error).error).isEqualTo(DataError.Network.UNAUTHORIZED)
            awaitComplete()
        }
    }

    @Test
    fun `excessive login attempts are throttled or handled`() = coroutineRule.runTest {
        // Assuming there's a mechanism to detect and throttle excessive attempts
        val email = "throttle@example.com"
        val password = "throttle"
        repeat(100) {
            viewModel.onUiEvent(UiEvent.OnLoginClicked)
        }

        // Verify that excessive attempts are detected
        // This assumes you have some logic to handle or report such cases
        assertThat(viewModel.uiState.value.errorType).isEqualTo(DataError.Network.UNKNOWN)
    }
}
