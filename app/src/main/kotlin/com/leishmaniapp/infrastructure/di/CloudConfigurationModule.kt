package com.leishmaniapp.infrastructure.di

import com.leishmaniapp.domain.services.IAuthorizationService
import com.leishmaniapp.infrastructure.service.cloud.GrpcServiceConfiguration
import com.leishmaniapp.infrastructure.http.AuthorizationInterceptor
import com.leishmaniapp.infrastructure.http.ExceptionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
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
    @Provides
    @Singleton
    fun provideGrpcServiceConfiguration(
        exceptionInterceptor: ExceptionInterceptor,
        authorizationInterceptor: AuthorizationInterceptor
    ): GrpcServiceConfiguration = GrpcServiceConfiguration(
        exceptionInterceptor = exceptionInterceptor,
        authorizationInterceptor = authorizationInterceptor
    )

    /**
     * Provide a global instance for [ExceptionInterceptor]
     */
    @Provides
    @Singleton
    fun provideExceptionInterceptor(): ExceptionInterceptor = ExceptionInterceptor()

    /**
     * Provide a global instance for [AuthorizationInterceptor] for authenticating requests
     */
    @Provides
    @Singleton
    fun provideAuthorizationInterceptior(
        authorizationService: IAuthorizationService,
        @InjectScopeWithIODispatcher coroutineScope: CoroutineScope,
    ): AuthorizationInterceptor = AuthorizationInterceptor(
        authorizationService = authorizationService,
        coroutineScope = coroutineScope
    )
}