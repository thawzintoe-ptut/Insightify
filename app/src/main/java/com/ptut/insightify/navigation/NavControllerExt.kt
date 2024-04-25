@file:Suppress("ktlint:standard:filename")

package com.ptut.insightify.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ptut.insightify.MainViewModel
import com.ptut.insightify.auth.navigation.login
import com.ptut.insightify.home.navigation.home
import com.ptut.insightify.navigation.util.Screen
import com.ptut.insightify.splash.navigation.splash


@Composable
fun SetupNavGraph(
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
    ) {
        splash {
            when (isLoggedIn) {
                true -> navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Splash.route) {
                        inclusive = true
                    }
                }

                false -> navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) {
                        inclusive = true
                    }
                }
            }
        }

        login(
            onLoginCompleted = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) {
                        inclusive = true
                    }
                }
            },
        )

        home(onDetailContinueClicked = { surveyId -> })
    }
}
