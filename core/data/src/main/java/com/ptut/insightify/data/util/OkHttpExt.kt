package com.ptut.insightify.data.util

import com.ptut.insightify.common.BuildConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Invocation

val API_TIMEOUT: Long
    get() = if (BuildConfig.DEBUG) 30L else 10L

fun Request.isAuthRequired(): Boolean = annotation(NoAuthRequired::class.java) == null

fun <T : Annotation> Request.annotation(annotationClass: Class<T>): T? =
    tag(Invocation::class.java)?.method()?.getAnnotation(annotationClass)

fun OkHttpClient.Builder.setLanguage(language: () -> String) =
    addInterceptor { chain ->
        val request = chain.request().newBuilder().addHeader("Accept-Language", language())
        chain.proceed(request.build())
    }

fun OkHttpClient.Builder.setPlatform(platform: String) =
    addInterceptor { chain ->
        val request = chain.request().newBuilder().addHeader("Platform", platform)
        chain.proceed(request.build())
    }

fun OkHttpClient.Builder.setAuthorizationToken(
    token: () -> String,
    refreshToken: () -> String
): OkHttpClient.Builder {
    addInterceptor { chain ->
        val request =
            if (chain.request().isAuthRequired()) {
                chain
                    .request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer ${token()}")
                    .build()
            } else {
                chain.request()
            }
        chain.proceed(request)
    }

    authenticator { _, response ->
        if (response.request.isAuthRequired() && response.priorResponse == null) {
            response.request
                .newBuilder()
                .removeHeader("Authorization")
                .addHeader("Authorization", "Bearer ${refreshToken()}")
                .build()
        } else if (response.request.isAuthRequired() && response.priorResponse != null) {
            throw ApiException(
                errorType = ApiErrorType.Unauthorized,
                message = "Unauthorized"
            )
        } else {
            null
        }
    }

    return this
}

fun OkHttpClient.Builder.handleErrors() =
    addInterceptor { chain ->
        val response = chain.proceed(chain.request())
        when {
            response.isSuccessful -> handleSuccessfulResponse(response)
            else -> throw ApiException(
                errorType = ApiErrorType.fromCode(response.code),
                message = response.message
            )
        }
    }

private fun handleSuccessfulResponse(response: Response): Response {
    if (response.code in listOf(204, 205)) {
        return response.modifyForNoContentResponse()
    }
    return response
}

private fun Response.modifyForNoContentResponse(): Response {
    val hasContent = this.body?.contentLength() != -1L
    if (hasContent) return this

    val emptyBody = "".toResponseBody("text/plain".toMediaType())
    return this.newBuilder()
        .code(200)
        .body(emptyBody)
        .build()
}
