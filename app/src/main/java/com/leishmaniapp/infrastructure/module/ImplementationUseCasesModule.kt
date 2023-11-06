package com.leishmaniapp.infrastructure.module

import com.leishmaniapp.infrastructure.cloud.CloudAuthenticationProvider
import com.leishmaniapp.infrastructure.cloud.CloudDiagnosisUpload
import com.leishmaniapp.infrastructure.cloud.CloudProcessingRequest
import com.leishmaniapp.usecases.IAuthenticationProvider
import com.leishmaniapp.usecases.IDiagnosisUpload
import com.leishmaniapp.usecases.IProcessingRequest
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ImplementationUseCasesModule {
    @Binds
    @Singleton
    abstract fun provideProcessingRequest(
        processingRequest: CloudProcessingRequest
    ): IProcessingRequest

    @Binds
    @Singleton
    abstract fun provideDiagnosisUpload(
        diagnosisUpload: CloudDiagnosisUpload
    ): IDiagnosisUpload

    @Singleton
    @Binds
    abstract fun provideAuthenticationProvider(
        authenticationProvider: CloudAuthenticationProvider
    ): IAuthenticationProvider
}