package com.ptut.insightify.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ptut.insightify.auth.LoginRoute

const val LOGIN = "login"

fun NavGraphBuilder.login(onLoginClick: () -> Unit) {
    composable(route = LOGIN) {
        LoginRoute(onLoginClick = onLoginClick)
    }
}
