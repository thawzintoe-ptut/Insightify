package com.ptut.insightify.data.util.refreshToken

import com.ptut.insightify.domain.user.repository.UserTokenProvider
import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED
import javax.inject.Inject

class TokenRefreshInterceptor @Inject constructor(
	private val tokenProvider: UserTokenProvider
) : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request()
		val response = chain.proceed(request)

		if (response.code == HTTP_UNAUTHORIZED) {
			val refreshToken = tokenProvider.getRefreshToken()

			// Retry the request with the new access token
			val newRequest = request.newBuilder()
				.header("Authorization", "Bearer $refreshToken")
				.build()
			return chain.proceed(newRequest)
		}

		return response
	}

}