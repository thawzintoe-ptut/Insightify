package com.ptut.insightify.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ptut.insightify.home.HomeRoute

const val HOME = "home"

fun NavGraphBuilder.home(
    onDetailContinueClicked: (String) -> Unit,
) {
    composable(route = HOME) {
        HomeRoute(
            viewModel = hiltViewModel(),
            onDetailContinueClicked = onDetailContinueClicked,
        )
    }
}
