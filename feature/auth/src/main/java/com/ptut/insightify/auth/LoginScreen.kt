package com.ptut.insightify.auth

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit
) {
    LoginScreen(
        modifier = modifier,
        onLoginClicked = onLoginClick
    )
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginClicked: () -> Unit
) {
    Text(
        modifier = modifier,
        text = "Login Screen"
    )
}
