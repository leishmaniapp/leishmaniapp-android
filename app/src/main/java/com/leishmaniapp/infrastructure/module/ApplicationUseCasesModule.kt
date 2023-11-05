package com.leishmaniapp.infrastructure.module

import com.leishmaniapp.infrastructure.ApplicationDiagnosisSharing
import com.leishmaniapp.infrastructure.ApplicationPictureStandardization
import com.leishmaniapp.infrastructure.cloud.CloudDiagnosisUpload
import com.leishmaniapp.infrastructure.cloud.CloudProcessingRequest
import com.leishmaniapp.usecases.IDiagnosisSharing
import com.leishmaniapp.usecases.IDiagnosisUpload
import com.leishmaniapp.usecases.IPictureStandardization
import com.leishmaniapp.usecases.IProcessingRequest
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationUseCasesModule {
    @Binds
    @Singleton
    abstract fun provideDiagnosisSharing(
        diagnosisSharing: ApplicationDiagnosisSharing
    ): IDiagnosisSharing

    @Binds
    @Singleton
    abstract fun providePictureStandardization(
        pictureStandardizationImpl: ApplicationPictureStandardization
    ): IPictureStandardization

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
}