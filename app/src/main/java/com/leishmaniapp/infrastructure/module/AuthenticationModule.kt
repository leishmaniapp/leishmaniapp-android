package com.leishmaniapp.infrastructure.module

import com.leishmaniapp.infrastructure.mock.MockAuthenticationProvider
import com.leishmaniapp.usecases.IAuthenticationProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {
    @Singleton
    @Provides
    fun provideAuthenticationProvider(): IAuthenticationProvider = MockAuthenticationProvider()
}