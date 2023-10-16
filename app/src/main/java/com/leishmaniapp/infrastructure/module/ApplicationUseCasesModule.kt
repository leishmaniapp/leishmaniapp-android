package com.leishmaniapp.infrastructure.module

import com.leishmaniapp.infrastructure.PDFDiagnosisSharing
import com.leishmaniapp.infrastructure.PictureStandardizationImpl
import com.leishmaniapp.usecases.IPictureStandardization
import com.leishmaniapp.usecases.types.IDiagnosisSharing
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
    abstract fun provideDiagnosisSharing(diagnosisSharing: PDFDiagnosisSharing): IDiagnosisSharing

    @Binds
    @Singleton
    abstract fun providePictureStandarization(pictureStandardizationImpl: PictureStandardizationImpl): IPictureStandardization
}