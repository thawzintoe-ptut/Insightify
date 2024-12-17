package com.ptut.insightify.navigation.util

import com.ptut.insightify.auth.navigation.LOGIN
import com.ptut.insightify.home.navigation.HOME
import com.ptut.insightify.splash.navigation.SPLASH

sealed class Route(val route: String) {
    data object Splash : Route(SPLASH)

    data object Login : Route(LOGIN)

    data object Home : Route(HOME)
}
