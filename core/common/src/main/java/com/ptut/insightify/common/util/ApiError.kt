package com.ptut.insightify.common.util

import java.io.IOException

data class ApiException(
    val errorType: ApiErrorType?,
    override val message: String = errorType?.description.orEmpty()
) : IOException()

enum class ApiErrorType(val code: Int, val description: String) {
    NoContent(204, "No Content"),
    BadRequest(400, "Bad Request"),
    Unauthorized(401, "Unauthorized"),
    PaymentRequired(402, "Payment Required"),
    Forbidden(403, "Forbidden"),
    NotFound(404, "Not Found"),
    MethodNotAllowed(405, "Method Not Allowed"),
    NotAcceptable(406, "Not Acceptable"),
    ProxyAuthenticationRequired(407, "Proxy Authentication Required"),
    RequestTimeout(408, "Request Timeout"),
    Gone(410, "Gone"),
    InternalServerError(500, "Internal Server Error"),
    NotImplemented(501, "Not Implemented"),
    BadGateway(502, "Bad Gateway"),
    ServiceUnavailable(503, "Service Unavailable"),
    GatewayTimeout(504, "Gateway Timeout"),
    HttpVersionNotSupported(505, "HTTP Version Not Supported");
    companion object {
        fun fromCode(code: Int?): ApiErrorType? = entries.find { it.code == code }
    }
}
