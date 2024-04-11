package com.ptut.insightify.data.util

import com.ptut.insightify.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

fun retrofit(
    init: Retrofit.Builder.() -> Unit
): Retrofit {
    val retrofit = Retrofit.Builder()
    retrofit.init()
    return retrofit.build()
}

fun Retrofit.Builder.okHttpClient(
    init: OkHttpClient.Builder.() -> Unit
): Retrofit.Builder {
    val okHttpClientBuilder = OkHttpClient.Builder()
    okHttpClientBuilder.init()
    if (BuildConfig.DEBUG) {
        okHttpClientBuilder.addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
    }
    val okHttpClient = okHttpClientBuilder.build()
    client(okHttpClient)
    return this
}
