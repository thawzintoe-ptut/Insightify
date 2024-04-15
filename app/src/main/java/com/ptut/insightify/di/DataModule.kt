package com.ptut.insightify.di

import com.ptut.insightify.data.login.remote.login.LoginRepositoryImpl
import com.ptut.insightify.data.login.service.LoginApiService
import com.ptut.insightify.domain.login.repository.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideLoginRepository(loginApiService: LoginApiService): LoginRepository {
        return LoginRepositoryImpl(loginApiService)
    }
}
