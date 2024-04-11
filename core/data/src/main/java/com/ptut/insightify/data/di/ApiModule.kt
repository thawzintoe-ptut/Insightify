package com.ptut.insightify.data.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ptut.insightify.data.util.API_TIMEOUT
import com.ptut.insightify.data.util.handleErrors
import com.ptut.insightify.data.util.okHttpClient
import com.ptut.insightify.data.util.retrofit
import com.ptut.insightify.data.util.setLanguage
import com.ptut.insightify.data.util.setPlatform
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            encodeDefaults = false
            explicitNulls = false
            isLenient = true
        }
    }

    @Provides
    fun provideChuckerInterceptor(
        @ApplicationContext context: Context,
    ): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(context)
            .collector(
                ChuckerCollector(
                    context = context,
                    showNotification = true,
                    retentionPeriod = RetentionManager.Period.ONE_HOUR,
                ),
            )
            .maxContentLength(250_000L)
            .redactHeaders("Auth-Token", "Bearer")
            .alwaysReadResponseBody(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        json: Json,
        chuckerInterceptor: ChuckerInterceptor,
    ) = retrofit {
//        baseUrl(BuildConfig.API_URL)
        okHttpClient {
            setPlatform("Android")
            setLanguage {
                Locale.getDefault().toLanguageTag()
            }
            addInterceptor(chuckerInterceptor)
            handleErrors()
            connectTimeout(API_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(API_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(API_TIMEOUT, TimeUnit.SECONDS)
        }
        addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    }
}
