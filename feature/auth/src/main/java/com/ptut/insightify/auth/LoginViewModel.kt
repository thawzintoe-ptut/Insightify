package com.ptut.insightify.auth

import androidx.compose.ui.focus.FocusDirection
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ptut.insightify.auth.util.FocusedTextFieldKey
import com.ptut.insightify.auth.util.IdConstants.USER_EMAIL
import com.ptut.insightify.auth.util.IdConstants.USER_PASSWORD
import com.ptut.insightify.auth.util.LoginViewModelConstants.FOCUSED_TEXT_FIELD
import com.ptut.insightify.common.Dispatcher
import com.ptut.insightify.common.InsightifyDispatchers.Default
import com.ptut.insightify.common.InsightifyDispatchers.IO
import com.ptut.insightify.ui.inputvalidations.InputValidator
import com.ptut.insightify.ui.inputvalidations.InputWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.ptut.insightify.ui.R as uiR

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val savedStateHandle: SavedStateHandle,
        @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
        @Dispatcher(Default) private val defaultDispatcher: CoroutineDispatcher
    ) : ViewModel() {
        private val userEmail = savedStateHandle.getStateFlow(USER_EMAIL, InputWrapper())
        private val userPassword = savedStateHandle.getStateFlow(USER_PASSWORD, InputWrapper())
        private var focusedTextField = savedStateHandle[FOCUSED_TEXT_FIELD] ?: FocusedTextFieldKey.EMAIL
            set(value) {
                field = value
                savedStateHandle[FOCUSED_TEXT_FIELD] = value
            }

        private val _uiState = MutableStateFlow(UiState())
        val uiState: StateFlow<UiState> = _uiState.asStateFlow()

        init {
            if (focusedTextField != FocusedTextFieldKey.NONE) focusOnLastSelectedTextField()
        }

        fun onUiEvent(event: UiEvent) {
            when (event) {
                is UiEvent.OnEmailChanged -> onEmailChanged(event.input)
                is UiEvent.OnPasswordChanged -> onPasswordChanged(event.input)
                is UiEvent.OnEmailImeActionClick -> {
                    focusedTextField = FocusedTextFieldKey.PASSWORD
                    _uiState.update {
                        it.copy(
                            isMoveFocused = true,
                            moveFocus = FocusDirection.Down
                        )
                    }
                }
                is UiEvent.OnPasswordImeActionClick -> {
                    _uiState.update {
                        it.copy(
                            isMoveFocused = true,
                            moveFocus = FocusDirection.Down
                        )
                    }
                }
                is UiEvent.OnTextFieldFocusChanged ->
                    onTextFieldFocusChanged(event.key, event.isFocused)

                is UiEvent.OnLoginClicked -> onLoginClicked()
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
            viewModelScope.launch(defaultDispatcher) {
                _uiState.update {
                    it.copy(
                        requestFocus = focusedTextField,
                        isKeyboardVisible = true
                    )
                }
            }
        }

        fun onEmailChanged(input: String) {
            val errorId = InputValidator.getEmailErrorIdOrNull(input)
            savedStateHandle[USER_EMAIL] = userEmail.value.copy(value = input, errorId = errorId)
            _uiState.update { state ->
                state.copy(
                    email = userEmail.value.copy(value = input, errorId = errorId),
                    areInputsValid = isEmailValid(userEmail.value) && isPasswordValid(userPassword.value)
                )
            }
        }

        fun onPasswordChanged(input: String) {
            val errorId = InputValidator.getPasswordErrorIdOrNull(input)
            savedStateHandle[USER_PASSWORD] = userPassword.value.copy(value = input, errorId = errorId)
            _uiState.update { state ->
                state.copy(
                    password = userPassword.value.copy(value = input, errorId = errorId),
                    areInputsValid = isEmailValid(userEmail.value) && isPasswordValid(userPassword.value)
                )
            }
        }

        private fun onTextFieldFocusChanged(
            key: FocusedTextFieldKey,
            isFocused: Boolean
        ) {
            focusedTextField = if (isFocused) key else FocusedTextFieldKey.NONE
        }

        private fun onLoginClicked() {
            val areInputsValid = isEmailValid(userEmail.value) && isPasswordValid(userPassword.value)
            viewModelScope.launch(defaultDispatcher) {
                if (areInputsValid) clearFocusAndHideKeyboard()
                val resId =
                    if (areInputsValid) {
                        uiR.string.login_success
                    } else {
                        uiR.string.validation_error
                    }
//            _events.send(UiEvent.ShowToast(resId))
            }
        }

        private fun clearFocusAndHideKeyboard() {
            _uiState.update {
                it.copy(
                    isFocusCleared = true,
                    isKeyboardVisible = false
                )
            }
            focusedTextField = FocusedTextFieldKey.NONE
        }

        data class UiState(
            val isLoading: Boolean = false,
            val email: InputWrapper = InputWrapper(),
            val password: InputWrapper = InputWrapper(),
            val areInputsValid: Boolean = false,
            val hasError: Boolean = false,
            val isKeyboardVisible: Boolean = false,
            val requestFocus: FocusedTextFieldKey = FocusedTextFieldKey.NONE,
            val isMoveFocused: Boolean = false,
            val moveFocus: FocusDirection = FocusDirection.Down,
            val isFocusCleared: Boolean = false,
            val showToast: Int? = null
        )

        sealed interface UiEvent {
            data class OnEmailChanged(val input: String) : UiEvent

            data class OnPasswordChanged(val input: String) : UiEvent

            data object OnEmailImeActionClick : UiEvent

            data object OnPasswordImeActionClick : UiEvent

            data class OnTextFieldFocusChanged(
                val key: FocusedTextFieldKey,
                val isFocused: Boolean
            ) : UiEvent

            data object OnLoginClicked : UiEvent
        }
    }
