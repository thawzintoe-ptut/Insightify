package com.ptut.insightify.auth.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ptut.insightify.auth.LoginRoute

const val LOGIN = "login"

fun NavGraphBuilder.login(
    onLoginCompleted: () -> Unit
) {
    composable(route = LOGIN) {
        LoginRoute(
            viewModel = hiltViewModel(),
            onLoginCompleted = onLoginCompleted
        )
    }
}
