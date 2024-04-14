package com.ptut.insightify.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ptut.insightify.auth.LoginViewModel.UiEvent
import com.ptut.insightify.auth.util.FocusedTextFieldKey
import com.ptut.insightify.auth.util.IdConstants.LOGIN_BTN
import com.ptut.insightify.auth.util.IdConstants.LOGIN_CONTENT
import com.ptut.insightify.auth.util.IdConstants.LOGIN_ICON
import com.ptut.insightify.auth.util.IdConstants.USER_EMAIL
import com.ptut.insightify.auth.util.IdConstants.USER_PASSWORD
import com.ptut.insightify.ui.Design
import com.ptut.insightify.ui.components.Button
import com.ptut.insightify.ui.components.PasswordField
import com.ptut.insightify.ui.components.TextField
import com.ptut.insightify.ui.inputvalidations.InputWrapper
import com.ptut.insightify.ui.theme.Black
import com.ptut.insightify.ui.theme.Black20
import com.ptut.insightify.ui.R as uiR

@Composable
fun LoginRoute(
    onLoginClick: () -> Unit,
    viewModel: LoginViewModel
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val emailFocusRequester: FocusRequester = remember { FocusRequester() }
    val passwordFocusRequester: FocusRequester = remember { FocusRequester() }

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
            FocusedTextFieldKey.NONE -> focusManager.clearFocus()
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
            onTextFieldFocusChanged(
                FocusedTextFieldKey.NONE,
                false
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors =
                                listOf(
                                    Black20,
                                    Black
                                )
                        )
                    ),
            painter = painterResource(id = uiR.drawable.ic_background),
            contentDescription = "login background"
        )
        LoginScreen(
            modifier =
                Modifier.fillMaxSize(),
            email = uiState.email,
            password = uiState.password,
            areInputsValid = uiState.areInputsValid,
            emailFocusRequester = emailFocusRequester,
            passwordFocusRequester = passwordFocusRequester,
            onEmailChanged = viewModel::onEmailChanged,
            onEmailImeAction = onEmailImeAction,
            onPasswordChanged = viewModel::onPasswordChanged,
            onPasswordImeAction = onPasswordImeAction,
            onEmailFieldFocusChanged = onTextFieldFocusChanged,
            onPasswordFieldFocusChanged = onTextFieldFocusChanged,
            onLoginClicked = {
                keyboardController?.hide()
                onLoginClick()
            }
        )
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
        isFocused: Boolean
    ) -> Unit,
    onPasswordFieldFocusChanged: (
        focusTextFieldKey: FocusedTextFieldKey,
        isFocused: Boolean
    ) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onPasswordImeAction: () -> Unit,
    onLoginClicked: () -> Unit
) {
    ConstraintLayout(
        constraintSet = setLoginConstraints(),
        modifier = modifier.padding(24.dp)
    ) {
        Image(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .size(48.dp)
                    .layoutId(LOGIN_ICON),
            painter = painterResource(id = uiR.drawable.ic_logo_white),
            contentDescription = "logo"
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
                                focusState.isFocused
                            )
                        },
                inputWrapper = email,
                labelResId = uiR.string.text_input_email,
                visualTransformation = VisualTransformation.None,
                onValueChange = onEmailChanged,
                onImeKeyAction = onEmailImeAction
            )
            Spacer(modifier = Modifier.height(10.dp))
            Design.Components.PasswordField(
                modifier =
                    Modifier.layoutId(USER_PASSWORD)
                        .focusRequester(passwordFocusRequester)
                        .onFocusChanged { focusState ->
                            onPasswordFieldFocusChanged(
                                FocusedTextFieldKey.PASSWORD,
                                focusState.isFocused
                            )
                        },
                inputWrapper = password,
                labelResId = uiR.string.text_input_password,
                visualTransformation = PasswordVisualTransformation('●'),
                onValueChange = onPasswordChanged,
                onImeKeyAction = onPasswordImeAction
            )
            Spacer(modifier = Modifier.height(10.dp))
            Design.Components.Button(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .layoutId(LOGIN_BTN),
                text = stringResource(uiR.string.btn_login),
                onClick = onLoginClicked,
                enabled = areInputsValid
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
