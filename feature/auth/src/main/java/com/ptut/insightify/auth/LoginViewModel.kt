package com.ptut.insightify.auth

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusDirection
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ptut.insightify.auth.util.FocusedTextFieldKey
import com.ptut.insightify.auth.util.IdConstants.USER_EMAIL
import com.ptut.insightify.auth.util.IdConstants.USER_PASSWORD
import com.ptut.insightify.auth.util.LoginViewModelConstants.FOCUSED_TEXT_FIELD
import com.ptut.insightify.common.error.DataError
import com.ptut.insightify.common.error.Result
import com.ptut.insightify.common.util.handleNetworkError
import com.ptut.insightify.domain.login.usecase.LoginUserUseCase
import com.ptut.insightify.ui.inputvalidations.InputValidator
import com.ptut.insightify.ui.inputvalidations.InputWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val loginUserUseCase: LoginUserUseCase,
) : ViewModel() {
    private var userEmail = savedStateHandle.get<InputWrapper>(USER_EMAIL) ?: InputWrapper()
    private var userPassword = savedStateHandle.get<InputWrapper>(USER_PASSWORD) ?: InputWrapper()
    private var focusedTextField = savedStateHandle[FOCUSED_TEXT_FIELD] ?: FocusedTextFieldKey.EMAIL
        set(value) {
            field = value
            savedStateHandle[FOCUSED_TEXT_FIELD] = value
        }

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _navigationEvent = Channel<NavigationEvent>(capacity = 1)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    init {
        if (focusedTextField != FocusedTextFieldKey.NONE) focusOnLastSelectedTextField()
    }

    fun onUiEvent(event: UiEvent) {
        when (event) {
            is UiEvent.OnEmailChanged -> handleEmailChanged(event.input)
            is UiEvent.OnPasswordChanged -> handlePasswordChanged(event.input)
            is UiEvent.OnEmailImeActionClick -> handleImeAction(
                FocusedTextFieldKey.PASSWORD,
                FocusDirection.Down,
            )

            is UiEvent.OnPasswordImeActionClick -> handleImeAction(
                FocusedTextFieldKey.NONE,
                FocusDirection.Down,
            )

            is UiEvent.OnTextFieldFocusChanged -> onTextFieldFocusChanged(
                event.key,
                event.isFocused,
            )

            is UiEvent.OnLoginClicked -> onLoginClicked()
            is UiEvent.RetryClicked -> onLoginClicked()
        }
    }

    // Validation for EMAIL and PASSWORD Field
    private fun isEmailValid(emailState: InputWrapper): Boolean {
        return emailState.value.isNotEmpty() && emailState.errorId == null
    }

    private fun isPasswordValid(passwordState: InputWrapper): Boolean {
        return passwordState.value.isNotEmpty() && passwordState.errorId == null
    }

    // Event for EMAIL and PASSWORD Field
    private fun focusOnLastSelectedTextField() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    requestFocus = focusedTextField,
                    isKeyboardVisible = true,
                )
            }
        }
    }

    fun handleEmailChanged(input: String) {
        val errorId = InputValidator.getEmailErrorIdOrNull(input)
        userEmail = userEmail.copy(
            value = input,
            errorId = errorId,
        )
        savedStateHandle[USER_EMAIL] = userEmail
        updateUiState()
    }

    fun handlePasswordChanged(input: String) {
        val errorId = InputValidator.getPasswordErrorIdOrNull(input)
        userPassword = userPassword.copy(
            value = input,
            errorId = errorId,
        )
        savedStateHandle[USER_PASSWORD] = userPassword
        updateUiState()
    }

    private fun updateUiState() {
        _uiState.update { currentState ->
            currentState.copy(
                email = userEmail,
                password = userPassword,
                areInputsValid = isEmailValid(userEmail) &&
                        isPasswordValid(userPassword),
                hasError = false,
                isKeyboardVisible = true,
                errorType = null
            )
        }
    }

    private fun onTextFieldFocusChanged(
        key: FocusedTextFieldKey,
        isFocused: Boolean,
    ) {
        focusedTextField = if (isFocused) key else FocusedTextFieldKey.NONE
        _uiState.update {
            it.copy(
                requestFocus = focusedTextField,
                moveFocus = when {
                    key == FocusedTextFieldKey.EMAIL && isFocused -> FocusDirection.Down
                    key == FocusedTextFieldKey.PASSWORD && isFocused -> FocusDirection.Up
                    else -> FocusDirection.Up
                },
            )
        }
    }

    private fun onLoginClicked() {
        val email = userEmail.value
        val password = userPassword.value
        _uiState.update { it.copy(isLoading = true, isKeyboardVisible = false) }
        loginUserUseCase(email, password)
            .onEach { result ->
                handleResult(result)
            }
            .catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        hasError = true,
                        errorType = handleNetworkError(exception),
                    )
                }
            }.launchIn(viewModelScope)
    }

    private fun handleResult(result: Result<Unit, DataError.Network>) {
        when (result) {
            is Result.Success -> {
                _uiState.update { it.copy(isLoading = false) }
                _navigationEvent.trySend(NavigationEvent.OnLoginCompleted)
            }

            is Result.Error -> {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        hasError = true,
                        errorType = result.error,
                    )
                }
            }
        }
    }

    private fun handleImeAction(
        focusTarget: FocusedTextFieldKey,
        focusDirection: FocusDirection,
    ) {
        focusedTextField = focusTarget
        _uiState.update {
            it.copy(
                isMoveFocused = true,
                moveFocus = focusDirection,
            )
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val email: InputWrapper = InputWrapper(),
        val password: InputWrapper = InputWrapper(),
        val areInputsValid: Boolean = false,
        val hasError: Boolean = false,
        val errorType: DataError.Network? = null,
        val isKeyboardVisible: Boolean = false,
        val requestFocus: FocusedTextFieldKey = FocusedTextFieldKey.NONE,
        val isMoveFocused: Boolean = false,
        val moveFocus: FocusDirection = FocusDirection.Down,
        val isFocusCleared: Boolean = false,
    )

    sealed interface UiEvent {
        data class OnEmailChanged(val input: String) : UiEvent

        data class OnPasswordChanged(val input: String) : UiEvent

        data object OnEmailImeActionClick : UiEvent

        data object OnPasswordImeActionClick : UiEvent

        data class OnTextFieldFocusChanged(
            val key: FocusedTextFieldKey,
            val isFocused: Boolean,
        ) : UiEvent

        data object OnLoginClicked : UiEvent

        data object RetryClicked : UiEvent
    }

    sealed interface NavigationEvent {
        data object OnLoginCompleted : NavigationEvent
    }
}
