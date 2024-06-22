package com.leishmaniapp.infrastructure.di

import com.leishmaniapp.infrastructure.cloud.GrpcServiceConfiguration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provide cloud configurations
 */
@Module
@InstallIn(SingletonComponent::class)
object CloudConfigurationModule {
    /**
     * Provide a global instance for [GrpcServiceConfiguration]
     */
    @Singleton
    @Provides
    fun provideGrpcServiceConfiguration(): GrpcServiceConfiguration = GrpcServiceConfiguration()
}