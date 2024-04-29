package com.ptut.insightify.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ptut.insightify.ui.Design
import com.ptut.insightify.ui.theme.Black
import com.ptut.insightify.ui.theme.Black20
import com.ptut.insightify.ui.theme.Typography

@Composable
fun Design.Components.Button(
    modifier: Modifier = Modifier,
    text: String,
    colors: ButtonColors =
        ButtonDefaults.filledTonalButtonColors(
            containerColor = Color.White,
            contentColor = Black20,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.Gray.copy(alpha = 0.38f)
        ),
    enabled: Boolean,
    onClick: () -> Unit
) {
    FilledTonalButton(
        modifier = modifier.height(64.dp),
        onClick = onClick,
        enabled = enabled,
        colors = colors,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            style =
                Typography.labelLarge.copy(
                    color = Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
            color = Black
        )
    }
}

@Preview
@Composable
fun ButtonPreview() {
    Design.Components.Button(text = "Button", enabled = true, onClick = {})
}
