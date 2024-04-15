package com.ptut.insightify.domain.di

import com.ptut.insightify.domain.login.repository.LoginRepository
import com.ptut.insightify.domain.login.usecase.LoginUserUseCase
import com.ptut.insightify.domain.user.repository.UserTokenProvider
import com.ptut.insightify.domain.user.usecase.UserLoggedInUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.multibindings.IntoSet

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    @ViewModelScoped
    @IntoSet
    fun provideLoginUseCase(
        userTokenProvider: UserTokenProvider,
        loginRepository: LoginRepository
    ): LoginUserUseCase {
        return LoginUserUseCase(
            userTokenProvider = userTokenProvider,
            loginRepository = loginRepository
        )
    }

    @Provides
    @ViewModelScoped
    @IntoSet
    fun provideUserLoggedInUseCase(
        userTokenProvider: UserTokenProvider
    ): UserLoggedInUseCase {
        return UserLoggedInUseCase(
            userTokenProvider = userTokenProvider
        )
    }
}
