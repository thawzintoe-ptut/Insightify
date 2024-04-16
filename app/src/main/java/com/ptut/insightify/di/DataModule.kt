package com.ptut.insightify.di

import com.ptut.insightify.data.login.remote.LoginRepositoryImpl
import com.ptut.insightify.data.login.service.LoginApiService
import com.ptut.insightify.data.profile.remote.ProfileRepositoryImpl
import com.ptut.insightify.data.profile.service.ProfileApiService
import com.ptut.insightify.data.survey.remote.SurveyRepositoryImpl
import com.ptut.insightify.data.survey.service.SurveyApiService
import com.ptut.insightify.domain.login.repository.LoginRepository
import com.ptut.insightify.domain.profile.repository.ProfileRepository
import com.ptut.insightify.domain.survey.repository.SurveyRepository
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

    @Provides
    @Singleton
    fun provideSurveyRepository(surveyApiService: SurveyApiService): SurveyRepository {
        return SurveyRepositoryImpl(surveyApiService)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(profileApiService: ProfileApiService): ProfileRepository {
        return ProfileRepositoryImpl(profileApiService)
    }
}
