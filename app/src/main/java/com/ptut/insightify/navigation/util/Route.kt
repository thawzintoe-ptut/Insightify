package com.ptut.insightify.navigation.util

import com.ptut.insightify.auth.navigation.LOGIN
import com.ptut.insightify.home.navigation.HOME
import com.ptut.insightify.splash.navigation.SPLASH

sealed class Screen(val route: String) {
    data object Splash : Screen(SPLASH)

    data object Login : Screen(LOGIN)

    data object Home : Screen(HOME)
}
