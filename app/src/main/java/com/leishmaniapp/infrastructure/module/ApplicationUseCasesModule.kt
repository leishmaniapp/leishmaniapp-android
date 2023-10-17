package com.leishmaniapp.infrastructure.module

import com.leishmaniapp.infrastructure.ApplicationPictureStandardization
import com.leishmaniapp.infrastructure.ApplicationDiagnosisSharing
import com.leishmaniapp.usecases.IDiagnosisSharing
import com.leishmaniapp.usecases.IPictureStandardization
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
}