@file:Suppress("ktlint:standard:filename")

package com.ptut.insightify.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ptut.insightify.MainViewModel
import com.ptut.insightify.auth.navigation.login
import com.ptut.insightify.auth.util.ObserveAsEvents
import com.ptut.insightify.navigation.util.Screen
import com.ptut.insightify.navigation.util.UiEvent

fun NavController.navigate(event: UiEvent.Navigate) {
    this.navigate(event.route)
}

@Composable
fun SetupNavGraph(navController: NavHostController) {
    val viewModel = hiltViewModel<MainViewModel>()
    ObserveAsEvents(viewModel.isLoggedIn) { isLoggedIn ->
        navController.navigate(
            if (isLoggedIn) {
                Screen.Home.route
            } else {
                Screen.Login.route
            },
        )
    }


    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
    ) {
        login(
            onLoginCompleted = {
                navController.navigate(
                    UiEvent.Navigate(Screen.Home.route),
                )
            },
        )
    }
}
