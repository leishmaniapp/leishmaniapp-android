package com.leishmaniapp.infrastructure.module

import com.leishmaniapp.infrastructure.cloud.CloudAuthenticationProvider
import com.leishmaniapp.usecases.IAuthenticationProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthenticationModule {
    @Singleton
    @Binds
    abstract fun provideAuthenticationProvider(authenticationProvider: CloudAuthenticationProvider): IAuthenticationProvider
}