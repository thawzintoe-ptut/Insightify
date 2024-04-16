package com.ptut.insightify.splash.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ptut.insightify.splash.SplashScreen

const val SPLASH = "splash"

fun NavGraphBuilder.splash(onSplashCompleted: () -> Unit) {
    composable(route = SPLASH) {
        SplashScreen(
            onSplashCompleted = onSplashCompleted,
        )
    }
}
