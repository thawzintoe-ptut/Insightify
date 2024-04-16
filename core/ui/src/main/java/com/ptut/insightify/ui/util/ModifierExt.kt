package com.ptut.insightify.ui.util

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun Modifier.gradientBackground(
    topColor: Color,
    bottomColor: Color,
) = this.then(
    Modifier.background(
        Brush.linearGradient(
            colors =
                listOf(
                    topColor,
                    bottomColor,
                ),
        ),
    ),
)
