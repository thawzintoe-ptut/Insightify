package com.ptut.insightify.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ptut.insightify.home.HomeRoute

const val HOME = "home"

fun NavGraphBuilder.home(innerPaddingValues: PaddingValues) {
    composable(route = HOME) {
        HomeRoute(innerPaddingValues)
    }
}
