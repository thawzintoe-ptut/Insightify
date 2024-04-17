package com.ptut.insightify.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ptut.insightify.ui.Design
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Preview
@Composable
fun Design.Components.ShimmerLoadingIndicator(modifier: Modifier = Modifier) {
    // Define the shimmer animation specification
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.Window)

    // Define the gradient of the shimmer
    val brush =
        Brush.linearGradient(
            colors =
            listOf(
                Color.White.copy(alpha = 0.9f),
                Color.White.copy(alpha = 0.2f),
                Color.White.copy(alpha = 0.9f),
            ),
            start = Offset(0f, 0f),
            end = Offset(500f, 500f),
        )

    // Shimmer animation card
    Box(modifier = modifier.padding(20.dp)) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 40.dp)
                .shimmer(shimmer),
        ) {
            Spacer(
                modifier = Modifier
                    .height(20.dp)
                    .background(brush, RoundedCornerShape(4.dp))
                    .fillMaxWidth(0.4f),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(
                    modifier = Modifier
                        .width(100.dp)
                        .height(25.dp)
                        .background(brush, RoundedCornerShape(4.dp))
                        .fillMaxWidth(0.7f),
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(brush, CircleShape),
                )
            }
        }

        Column(
            Modifier
                .padding(20.dp)
                .align(Alignment.BottomStart)
                .shimmer(shimmer),
        ) {
            Spacer(
                modifier =
                Modifier
                    .height(25.dp)
                    .background(brush, RoundedCornerShape(4.dp))
                    .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(20.dp))
            Spacer(
                modifier =
                Modifier
                    .width(100.dp)
                    .height(20.dp)
                    .background(brush, RoundedCornerShape(4.dp))
                    .fillMaxWidth(0.6f),
            )
            Spacer(
                modifier =
                Modifier
                    .width(100.dp)
                    .height(20.dp)
                    .background(brush, RoundedCornerShape(4.dp))
                    .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
