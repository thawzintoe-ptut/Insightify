package com.ptut.insightify.common.util

import com.ptut.insightify.common.error.DataError
import com.ptut.insightify.common.error.Result
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun handleError(
    exception: Throwable,
    send: suspend (Result.Error<Nothing,DataError.Network>) -> Unit
) {
    when (exception) {
        is ApiException -> {
            val message =
                if (exception.errorType != null) {
                    "${exception.errorType.code} - ${exception.errorType.description}"
                } else {
                    "Unknown"
                }
            if (exception.errorType == ApiErrorType.Unauthorized) {
                Timber.d(message = message, t = exception)
            } else {
                Timber.w(message = message, t = exception)
            }
            send(
                Result.Error(
                    error = when (exception.errorType) {
                        ApiErrorType.NoContent -> DataError.Network.NO_CONTENT
                        ApiErrorType.BadRequest -> DataError.Network.BAD_REQUEST
                        ApiErrorType.Unauthorized -> DataError.Network.UNAUTHORIZED
                        ApiErrorType.Forbidden -> DataError.Network.FORBIDDEN
                        ApiErrorType.NotFound -> DataError.Network.NOT_FOUND
                        ApiErrorType.RequestTimeout -> DataError.Network.REQUEST_TIME_OUT
                        ApiErrorType.Gone -> DataError.Network.GONE
                        ApiErrorType.InternalServerError -> DataError.Network.INTERNAL_SERVER_ERROR
                        ApiErrorType.BadGateway -> DataError.Network.BAD_GATEWAY
                        ApiErrorType.ServiceUnavailable -> DataError.Network.SERVICE_UNAVAILABLE
                        else -> DataError.Network.UNKNOWN
                    }
                )
            )
        }
        is SocketTimeoutException,
        is UnknownHostException,
        is ConnectException -> {
            Timber.d(message = "Network Unavailable", t = exception)
            send(Result.Error(error = DataError.Network.NETWORK_UNAVAILABLE))
        }

        else -> {
            Timber.w(message = "Unknown", t = exception)
            send(Result.Error(error = DataError.Network.UNKNOWN))
        }
    }
}
