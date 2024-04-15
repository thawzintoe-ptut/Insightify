package com.ptut.insightify.common.error

sealed interface DataError : Error {
    enum class Network : DataError {
        NO_CONTENT,
        BAD_REQUEST,
        NOT_FOUND,
        REQUEST_TIME_OUT,
        GONE,
        INTERNAL_SERVER_ERROR,
        BAD_GATEWAY,
        SERVICE_UNAVAILABLE,
        FORBIDDEN,
        UNKNOWN,
        UNAUTHORIZED,
        NETWORK_UNAVAILABLE
    }

    enum class Local : DataError {
        DISK_FULL,
    }
}
