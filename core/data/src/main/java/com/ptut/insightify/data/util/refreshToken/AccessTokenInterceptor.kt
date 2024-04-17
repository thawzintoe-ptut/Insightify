package com.ptut.insightify.data.util.refreshToken

import com.ptut.insightify.domain.user.repository.UserTokenProvider
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AccessTokenInterceptor
    @Inject
    constructor(private val tokenProvider: UserTokenProvider) :
    Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val accessToken = tokenProvider.getAccessToken()
            val request =
                chain.request().newBuilder()
                    .header("Authorization", "Bearer $accessToken")
                    .build()
            return chain.proceed(request)
        }
    }
