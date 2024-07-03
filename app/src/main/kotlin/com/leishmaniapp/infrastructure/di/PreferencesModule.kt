package com.leishmaniapp.infrastructure.di

import com.leishmaniapp.domain.services.IAuthorizationService
import com.leishmaniapp.infrastructure.preferences.DataStoreAuthorizationServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Provide the Shared Preferences implementation repositories
 */
@Module
@InstallIn(SingletonComponent::class)
interface PreferencesModule {

    /**
     * Provide the [IAuthorizationService] via [DataStoreAuthorizationServiceImpl]
     */
    @Binds
    fun bindTokenRepository(
        tokenRepositoryImpl: DataStoreAuthorizationServiceImpl,
    ): IAuthorizationService
}