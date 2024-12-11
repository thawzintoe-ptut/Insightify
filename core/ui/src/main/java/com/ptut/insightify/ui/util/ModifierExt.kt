package com.ptut.insightify.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import kotlin.math.tan

@Composable
fun Modifier.gradientBackground(
    topColor: Color,
    bottomColor: Color,
): Modifier {
    return this.drawWithCache {
        // Compute the start and end coordinates such that the gradients are angled 11.06
        // degrees off the vertical axis
        val offset = size.height * tan(
            Math
                .toRadians(11.06)
                .toFloat(),
        )

        val start = Offset(size.width / 2 + offset / 2, 0f)
        val end = Offset(size.width / 2 - offset / 2, size.height)

        // Create the top gradient that fades out after the halfway point vertically
        val topGradient = Brush.linearGradient(
            0f to if (topColor == Color.Unspecified) {
                Color.Transparent
            } else {
                topColor
            },
            0.724f to Color.Transparent,
            start = start,
            end = end,
        )
        // Create the bottom gradient that fades in before the halfway point vertically
        val bottomGradient = Brush.linearGradient(
            0.2552f to Color.Transparent,
            1f to if (bottomColor == Color.Unspecified) {
                Color.Transparent
            } else {
                bottomColor
            },
            start = start,
            end = end,
        )

        onDrawBehind {
            // There is overlap here, so order is important
            drawRect(topGradient)
            drawRect(bottomGradient)
        }
    }
}

fun createGradientBrush(
    colors: List<Color>,
    isVertical: Boolean = true,
): Brush {
    val endOffset = if (isVertical) {
        Offset(0f, Float.POSITIVE_INFINITY)
    } else {
        Offset(Float.POSITIVE_INFINITY, 0f)
    }

    return Brush.linearGradient(
        colors = colors,
        start = Offset(0f, 0f),
        end = endOffset,
        tileMode = TileMode.Clamp,
    )
}
