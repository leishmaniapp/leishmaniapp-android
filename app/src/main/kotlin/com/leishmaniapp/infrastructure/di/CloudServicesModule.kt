package com.leishmaniapp.infrastructure.di

import com.leishmaniapp.domain.services.IAnalysisService
import com.leishmaniapp.domain.services.IAuthService
import com.leishmaniapp.domain.services.IAvailabilityService
import com.leishmaniapp.infrastructure.service.cloud.GrpcAnalysisServiceImpl
import com.leishmaniapp.infrastructure.service.cloud.GrpcAuthServiceImpl
import com.leishmaniapp.infrastructure.service.cloud.GrpcAvailabilityServiceImpl
import com.leishmaniapp.infrastructure.service.cloud.GrpcServiceConfiguration
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides the services implementations via DI for cloud based services
 * **Based on LeishmaniappCloudServicesV2**
 */
@Module
@InstallIn(SingletonComponent::class)
interface CloudServicesModule {
    /**
     * Provide [IAuthService] via [GrpcAuthServiceImpl]
     */
    @Binds
    @Singleton
    fun bindAuthService(
        authServiceImpl: GrpcAuthServiceImpl,
    ): IAuthService

    /**
     * Provide [IAnalysisService] via [GrpcAnalysisServiceImpl]
     */
    @Binds
    @Singleton
    fun bindAnalysisService(
        analysisServiceImpl: GrpcAnalysisServiceImpl
    ): IAnalysisService

    /**
     * Provide the [IAvailabilityService] via [GrpcAvailabilityServiceImpl]
     */
    @Binds
    @Singleton
    fun bindAvailabilityService(
        networkService: GrpcAvailabilityServiceImpl
    ): IAvailabilityService
}