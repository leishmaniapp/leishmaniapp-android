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
annotation class InjectDefaultDispatcher

/**
 * Provides [Dispatchers.IO] as dispatcher for [CoroutineScope]
 */
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class InjectIODispatcher

/**
 * Provide an application wide [CoroutineScope]
 */
@InstallIn(SingletonComponent::class)
@Module
class CoroutineScopeModule {
    @Provides
    @Singleton
    @InjectDefaultDispatcher
    fun provideDefaultCoroutineScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @Provides
    @Singleton
    @InjectIODispatcher
    fun provideIOCoroutineScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
}

