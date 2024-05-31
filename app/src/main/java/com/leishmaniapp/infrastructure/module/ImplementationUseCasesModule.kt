package com.leishmaniapp.infrastructure.module

import com.leishmaniapp.infrastructure.mock.MockAuthenticationProvider
import com.leishmaniapp.infrastructure.mock.MockDiagnosisUpload
import com.leishmaniapp.infrastructure.mock.MockProcessingRequest
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
        processingRequest: MockProcessingRequest
    ): IProcessingRequest

    @Binds
    @Singleton
    abstract fun provideDiagnosisUpload(
        diagnosisUpload: MockDiagnosisUpload
    ): IDiagnosisUpload

    @Singleton
    @Binds
    abstract fun provideAuthenticationProvider(
        authenticationProvider: MockAuthenticationProvider
    ): IAuthenticationProvider
}