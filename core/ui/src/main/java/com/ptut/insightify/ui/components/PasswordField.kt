@file:OptIn(ExperimentalMaterial3Api::class)

package com.ptut.insightify.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ptut.insightify.ui.Design
import com.ptut.insightify.ui.inputvalidations.InputWrapper
import com.ptut.insightify.ui.theme.Typography
import com.ptut.insightify.ui.theme.White
import com.ptut.insightify.ui.theme.White18
import com.ptut.insightify.ui.util.OnImeKeyAction
import com.ptut.insightify.ui.util.OnValueChange

@Composable
fun Design.Components.PasswordField(
    modifier: Modifier = Modifier,
    inputWrapper: InputWrapper,
    @StringRes labelResId: Int,
    keyboardOptions: KeyboardOptions =
        remember {
            KeyboardOptions.Default
        },
    visualTransformation: VisualTransformation =
        remember {
            VisualTransformation.None
        },
    onValueChange: OnValueChange,
    onImeKeyAction: OnImeKeyAction
) {
    val fieldValue =
        remember {
            mutableStateOf(TextFieldValue(inputWrapper.value, TextRange(inputWrapper.value.length)))
        }
    Column(modifier = modifier) {
        OutlinedTextField(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(
                        shape = RoundedCornerShape(16.dp),
                        color = White18
                    ),
            value = fieldValue.value,
            onValueChange = {
                fieldValue.value = it
                onValueChange(it.text)
            },
            visualTransformation = visualTransformation,
            singleLine = true,
            colors =
                outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = White
                ),
            placeholder = {
                Row(Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(labelResId),
                        style =
                            Typography.labelLarge.copy(
                                color = White,
                                fontSize = 20.sp
                            ),
                        color = White.copy(alpha = 0.3f)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        modifier =
                            Modifier.clickable(
                                onClick = {
                                    // TODO: implement forget password
                                }
                            ),
                        text = "Forgot?",
                        style =
                            Typography.labelLarge.copy(
                                color = White,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Right
                            ),
                        color = White.copy(alpha = 0.3f)
                    )
                }
            },
            textStyle =
                Typography.labelLarge.copy(
                    color = White,
                    fontSize = 20.sp
                ),
            keyboardOptions = keyboardOptions,
            keyboardActions =
                remember {
                    KeyboardActions(onAny = { onImeKeyAction() })
                }
        )
        Spacer(modifier = Modifier.height(12.dp))
        if (inputWrapper.errorId != null) {
            Text(
                text = stringResource(inputWrapper.errorId),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}
