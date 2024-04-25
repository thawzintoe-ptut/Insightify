package com.ptut.insightify.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.android.material.textfield.TextInputLayout.BoxBackgroundMode
import com.ptut.insightify.auth.LoginViewModel.UiEvent
import com.ptut.insightify.auth.util.FocusedTextFieldKey
import com.ptut.insightify.auth.util.IdConstants.LOGIN_BTN
import com.ptut.insightify.auth.util.IdConstants.LOGIN_CONTENT
import com.ptut.insightify.auth.util.IdConstants.LOGIN_ICON
import com.ptut.insightify.auth.util.IdConstants.USER_EMAIL
import com.ptut.insightify.auth.util.IdConstants.USER_PASSWORD
import com.ptut.insightify.common.error.DataError
import com.ptut.insightify.ui.Design
import com.ptut.insightify.ui.R
import com.ptut.insightify.ui.components.Button
import com.ptut.insightify.ui.components.LoadingWheel
import com.ptut.insightify.ui.components.PasswordField
import com.ptut.insightify.ui.components.TextField
import com.ptut.insightify.ui.inputvalidations.InputWrapper
import com.ptut.insightify.ui.util.ObserveAsEvents
import com.ptut.insightify.ui.R as uiR

@Composable
fun LoginRoute(
    viewModel: LoginViewModel,
    onLoginCompleted: () -> Unit,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val emailFocusRequester: FocusRequester = remember { FocusRequester() }
    val passwordFocusRequester: FocusRequester = remember { FocusRequester() }

    ObserveAsEvents(flow = viewModel.navigationEvent) { navigationEvent ->
        when (navigationEvent) {
            is LoginViewModel.NavigationEvent.OnLoginCompleted -> onLoginCompleted()
        }
    }

    val onTextFieldFocusChanged = { focusTextFieldKey: FocusedTextFieldKey, isFocused: Boolean ->
        viewModel.onUiEvent(UiEvent.OnTextFieldFocusChanged(focusTextFieldKey, isFocused))
    }

    val onEmailImeAction = {
        viewModel.onUiEvent(UiEvent.OnEmailImeActionClick)
    }

    val onPasswordImeAction = {
        keyboardController?.hide()
        viewModel.onUiEvent(UiEvent.OnPasswordImeActionClick)
    }

    LaunchedEffect(Lifecycle.State.STARTED) {
        when (uiState.requestFocus) {
            FocusedTextFieldKey.EMAIL -> emailFocusRequester.requestFocus()
            FocusedTextFieldKey.PASSWORD -> passwordFocusRequester.requestFocus()
            FocusedTextFieldKey.NONE -> {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        }
    }

    if (uiState.isMoveFocused) {
        focusManager.moveFocus(uiState.moveFocus)
    }
    if (uiState.isKeyboardVisible) {
        keyboardController?.show()
    } else {
        keyboardController?.hide()
    }
    if (uiState.isFocusCleared) {
        focusManager.clearFocus()
        keyboardController?.hide()
        onTextFieldFocusChanged(FocusedTextFieldKey.NONE, false)
    }


    Box {
        LoginScreenBackground(modifier = Modifier.fillMaxSize())
        if (uiState.isLoading) {
            Design.Components.LoadingWheel(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                    .pointerInput(Unit) {},
            )
        }

        LoginScreen(
            modifier = Modifier.fillMaxSize(),
            email = uiState.email,
            password = uiState.password,
            areInputsValid = uiState.areInputsValid,
            emailFocusRequester = emailFocusRequester,
            passwordFocusRequester = passwordFocusRequester,
            onEmailChanged = viewModel::handleEmailChanged,
            onEmailImeAction = onEmailImeAction,
            onPasswordChanged = viewModel::handlePasswordChanged,
            onPasswordImeAction = onPasswordImeAction,
            onEmailFieldFocusChanged = onTextFieldFocusChanged,
            onPasswordFieldFocusChanged = onTextFieldFocusChanged,
            onLoginClicked = {
                keyboardController?.hide()
                viewModel.onUiEvent(UiEvent.OnLoginClicked)
            },
        )

        if (uiState.hasError) {
            val resId = when (uiState.errorType) {
                DataError.Network.NO_CONTENT -> R.string.error_no_content
                DataError.Network.BAD_GATEWAY -> R.string.error_bad_gateway
                DataError.Network.FORBIDDEN -> R.string.error_forbidden
                DataError.Network.NOT_FOUND -> R.string.error_not_found
                DataError.Network.REQUEST_TIME_OUT -> R.string.error_request_time_out
                DataError.Network.SERVICE_UNAVAILABLE -> R.string.error_service_unavailable
                DataError.Network.INTERNAL_SERVER_ERROR -> R.string.error_internal_server_error
                DataError.Network.BAD_REQUEST -> R.string.error_bad_request
                DataError.Network.UNKNOWN -> R.string.error_unknown
                DataError.Network.NETWORK_UNAVAILABLE -> R.string.error_network_unavailable
                DataError.Network.UNAUTHORIZED -> R.string.error_unauthorized
                DataError.Network.GONE -> R.string.error_gone
                null -> R.string.error_unknown
            }
            Toast.makeText(
                context,
                stringResource(resId),
                Toast.LENGTH_LONG,
            ).show()
        }
    }
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    email: InputWrapper,
    password: InputWrapper,
    areInputsValid: Boolean,
    emailFocusRequester: FocusRequester,
    passwordFocusRequester: FocusRequester,
    onEmailChanged: (String) -> Unit,
    onEmailImeAction: () -> Unit,
    onEmailFieldFocusChanged: (
        focusTextFieldKey: FocusedTextFieldKey,
        isFocused: Boolean,
    ) -> Unit,
    onPasswordFieldFocusChanged: (
        focusTextFieldKey: FocusedTextFieldKey,
        isFocused: Boolean,
    ) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onPasswordImeAction: () -> Unit,
    onLoginClicked: () -> Unit,
) {
    ConstraintLayout(
        constraintSet = setLoginConstraints(),
        modifier = modifier.padding(24.dp),
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .size(48.dp)
                .layoutId(LOGIN_ICON),
            painter = painterResource(id = uiR.drawable.ic_logo_white),
            contentDescription = "logo",
        )
        Column(modifier = Modifier.layoutId(LOGIN_CONTENT)) {
            Design.Components.TextField(
                modifier =
                Modifier
                    .layoutId(USER_EMAIL)
                    .focusRequester(emailFocusRequester)
                    .onFocusChanged { focusState ->
                        onEmailFieldFocusChanged(
                            FocusedTextFieldKey.EMAIL,
                            focusState.isFocused,
                        )
                    },
                inputWrapper = email,
                labelResId = uiR.string.text_input_email,
                visualTransformation = VisualTransformation.None,
                onValueChange = onEmailChanged,
                onImeKeyAction = onEmailImeAction,
                keyboardOptions =
                KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Design.Components.PasswordField(
                modifier =
                Modifier
                    .layoutId(USER_PASSWORD)
                    .focusRequester(passwordFocusRequester)
                    .onFocusChanged { focusState ->
                        onPasswordFieldFocusChanged(
                            FocusedTextFieldKey.PASSWORD,
                            focusState.isFocused,
                        )
                    },
                inputWrapper = password,
                labelResId = uiR.string.text_input_password,
                visualTransformation = PasswordVisualTransformation('●'),
                onValueChange = onPasswordChanged,
                onImeKeyAction = onPasswordImeAction,
                keyboardOptions =
                KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Design.Components.Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .layoutId(LOGIN_BTN),
                text = stringResource(uiR.string.btn_login),
                onClick = onLoginClicked,
                enabled = areInputsValid,
            )
        }
    }
}

@Composable
fun setLoginConstraints(): ConstraintSet {
    return ConstraintSet {
        val loginImageConstraint = createRefFor(LOGIN_ICON)
        val loginContentConstraint = createRefFor(LOGIN_CONTENT)

        constrain(loginImageConstraint) {
            top.linkTo(parent.top, margin = 159.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
            height = Dimension.wrapContent
        }
        constrain(loginContentConstraint) {
            top.linkTo(loginImageConstraint.bottom, margin = 117.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
            height = Dimension.wrapContent
        }
    }
}

@Composable
fun LoginScreenBackground(modifier: Modifier = Modifier) {
    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(id = uiR.drawable.ic_background),
        contentDescription = "login background",
        contentScale = ContentScale.FillBounds
    )
}
