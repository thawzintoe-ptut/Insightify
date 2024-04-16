package com.ptut.insightify.data.di

import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ptut.insightify.common.BuildConfig
import com.ptut.insightify.data.login.local.user.UserTokenSharedPreferencesProvider
import com.ptut.insightify.data.login.service.LoginApiService
import com.ptut.insightify.data.profile.service.ProfileApiService
import com.ptut.insightify.data.survey.service.SurveyApiService
import com.ptut.insightify.data.util.API_TIMEOUT
import com.ptut.insightify.data.util.handleErrors
import com.ptut.insightify.data.util.okHttpClient
import com.ptut.insightify.data.util.refreshToken.AccessTokenInterceptor
import com.ptut.insightify.data.util.refreshToken.TokenRefreshInterceptor
import com.ptut.insightify.data.util.retrofit
import com.ptut.insightify.data.util.setAuthorizationToken
import com.ptut.insightify.data.util.setLanguage
import com.ptut.insightify.data.util.setPlatform
import com.ptut.insightify.domain.user.repository.UserTokenProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

const val ENCRYPTED_PREFERENCES_NAME = "ENCRYPTED_PREFERENCES_NAME"

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @RequiresApi(Build.VERSION_CODES.M)
    @Provides
    @Singleton
    fun provideMasterKey(
        @ApplicationContext context: Context,
    ): MasterKey =
        MasterKey.Builder(context).setKeyGenParameterSpec(
            KeyGenParameterSpec.Builder(
                MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
            ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE).build(),
        ).build()

    @Provides
    @Singleton
    fun provideTokenProvider(
        @ApplicationContext context: Context,
        masterKey: MasterKey,
    ): UserTokenProvider {
        return UserTokenSharedPreferencesProvider(
            EncryptedSharedPreferences.create(
                context,
                ENCRYPTED_PREFERENCES_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
            ),
        )
    }

    @Provides
    @Singleton
    fun provideAccessTokenInterceptor(tokenProvider: UserTokenProvider): AccessTokenInterceptor {
        return AccessTokenInterceptor(tokenProvider)
    }

    @Provides
    @Singleton
    fun provideTokenRefreshInterceptor(tokenProvider: UserTokenProvider): TokenRefreshInterceptor {
        return TokenRefreshInterceptor(tokenProvider)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            encodeDefaults = false
            explicitNulls = false
            isLenient = true
        }
    }

    @Provides
    @Singleton
    fun provideChuckerInterceptor(
        @ApplicationContext context: Context,
    ): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(context).collector(
            ChuckerCollector(
                context = context,
                showNotification = true,
                retentionPeriod = RetentionManager.Period.ONE_HOUR,
            ),
        ).maxContentLength(250_000L).redactHeaders("Auth-Token", "Bearer")
            .alwaysReadResponseBody(true).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        json: Json,
        chuckerInterceptor: ChuckerInterceptor,
        userTokenProvider: UserTokenProvider,
    ) = retrofit {
        baseUrl(BuildConfig.API_BASE_URL)
        okHttpClient {
            setPlatform("Android")
            setLanguage {
                Locale.getDefault().toLanguageTag()
            }
            if (BuildConfig.DEBUG) {
                addInterceptor(chuckerInterceptor)
            }
            setAuthorizationToken(
                token = {
                    userTokenProvider.getAccessToken()
                },
                refreshToken = {
                    userTokenProvider.getRefreshToken()
                },
            )
            handleErrors()
            connectTimeout(API_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(API_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(API_TIMEOUT, TimeUnit.SECONDS)
        }
        addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    }

    @Provides
    @Singleton
    fun provideLoginApiService(retrofit: Retrofit): LoginApiService {
        return retrofit.create(LoginApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSurveyApiService(retrofit: Retrofit): SurveyApiService {
        return retrofit.create(SurveyApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileApiService(retrofit: Retrofit): ProfileApiService {
        return retrofit.create(ProfileApiService::class.java)
    }
}
