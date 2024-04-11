package com.ptut.insightify.navigation.util

import com.ptut.insightify.auth.navigation.LOGIN

sealed class Screen(val route: String) {
    data object Home : Screen("home")

    data object Login : Screen(LOGIN)
}
