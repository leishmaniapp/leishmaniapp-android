package com.leishmaniapp.infrastructure.di

import com.leishmaniapp.domain.repository.ITokenRepository
import com.leishmaniapp.infrastructure.preferences.DataStoreTokenRepositoryImpl
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
     * Provide the [ITokenRepository] via [DataStoreTokenRepositoryImpl]
     */
    @Binds
    fun bindTokenRepository(
        tokenRepositoryImpl: DataStoreTokenRepositoryImpl,
    ): ITokenRepository

}