package com.ptut.insightify.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ptut.insightify.common.error.DataError
import com.ptut.insightify.ui.Design
import com.ptut.insightify.ui.R
import com.ptut.insightify.ui.components.Button

@Composable
fun Design.Components.ErrorScreen(
    modifier: Modifier = Modifier,
    errorType: DataError.Network,
    onActionButtonClick: () -> Unit,
) {
    val resId =
        when (errorType) {
            DataError.Network.NO_CONTENT -> R.string.error_no_content
            DataError.Network.BAD_GATEWAY -> R.string.error_bad_gateway
            DataError.Network.FORBIDDEN -> R.string.error_forbidden
            DataError.Network.NOT_FOUND -> R.string.error_not_found
            DataError.Network.REQUEST_TIME_OUT -> R.string.error_request_time_out
            DataError.Network.SERVICE_UNAVAILABLE -> R.string.error_service_unavailable
            DataError.Network.INTERNAL_SERVER_ERROR -> R.string.error_internal_server_error
            DataError.Network.BAD_REQUEST -> R.string.error_bad_request
            DataError.Network.UNKNOWN -> R.string.error_unknown
            DataError.Network.NETWORK_UNAVAILABLE -> R.string.error_network_unavailable
            DataError.Network.UNAUTHORIZED -> R.string.error_unauthorized
            DataError.Network.GONE -> R.string.error_gone
        }

    Column(
        modifier =
        modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            modifier = Modifier.aspectRatio(1f),
            painter = painterResource(id = R.drawable.illustration_error_globe),
            contentDescription = stringResource(resId),
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier =
            Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 40.dp),
            text = stringResource(resId),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            minLines = 2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(30.dp))
        Design.Components.Button(
            modifier =
            Modifier
                .fillMaxWidth()
                .semantics {
                    role = Role.Button
                },
            text = stringResource(id = R.string.btn_retry),
            colors = ButtonDefaults.filledTonalButtonColors(),
            onClick = onActionButtonClick,
            enabled = true,
        )
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun ErrorScreenPreview() {
    Design.Components.ErrorScreen(
        errorType = DataError.Network.NO_CONTENT,
        onActionButtonClick = {}
    )
}
