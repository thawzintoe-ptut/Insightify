@file:Suppress("ktlint:standard:filename")

package com.ptut.insightify.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ptut.insightify.auth.navigation.login
import com.ptut.insightify.navigation.util.Screen
import com.ptut.insightify.navigation.util.UiEvent

fun NavController.navigate(event: UiEvent.Navigate) {
    this.navigate(event.route)
}

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        login(
            onLoginClick = {}
        )
    }
}
