@file:Suppress("ktlint:standard:filename")

package com.ptut.insightify.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ptut.insightify.MainViewModel
import com.ptut.insightify.auth.navigation.login
import com.ptut.insightify.home.navigation.home
import com.ptut.insightify.navigation.util.Screen
import com.ptut.insightify.navigation.util.UiEvent
import com.ptut.insightify.splash.navigation.splash

fun NavController.navigate(event: UiEvent.Navigate) {
    this.navigate(event.route)
}

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    padding: PaddingValues,
) {
    val viewModel = hiltViewModel<MainViewModel>()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
    ) {
        splash {
            when (isLoggedIn) {
                true -> navController.navigate(UiEvent.Navigate(Screen.Home.route))
                false -> navController.navigate(UiEvent.Navigate(Screen.Login.route))
            }
        }

        login(
            onLoginCompleted = {
                navController.navigate(
                    UiEvent.Navigate(Screen.Home.route),
                )
            },
        )
        home(innerPaddingValues = padding)
    }
}
