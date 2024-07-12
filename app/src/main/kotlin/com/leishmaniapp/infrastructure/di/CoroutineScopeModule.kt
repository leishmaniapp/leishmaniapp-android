package com.leishmaniapp.infrastructure.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Provides [Dispatchers.Default] as dispatcher for [CoroutineScope]
 */
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class InjectScopeWithDefaultDispatcher

/**
 * Provides [Dispatchers.IO] as dispatcher for [CoroutineScope]
 */
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class InjectScopeWithIODispatcher

/**
 * Provide an application wide [CoroutineScope]
 */
@InstallIn(SingletonComponent::class)
@Module
class CoroutineScopeModule {
    @Provides
    @Singleton
    @InjectScopeWithDefaultDispatcher
    fun provideDefaultCoroutineScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @Provides
    @Singleton
    @InjectScopeWithIODispatcher
    fun provideIOCoroutineScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
}

